package com.sbm.service;

import com.sbm.dto.*;
import com.sbm.entity.*;
import com.sbm.exception.*;
import com.sbm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final BusinessRepository businessRepository;

    @Transactional(readOnly = true)
    public Page<SaleDto> getAllByBusiness(Long businessId, Pageable pageable) {
        return saleRepository.findByBusinessId(businessId, pageable).map(this::toDto);
    }

    public SaleDto getById(Long id, Long businessId) {
        Sale sale = saleRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return toDto(sale);
    }

    @Transactional
    public SaleDto create(SaleDto dto, Long businessId, Long userId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        Customer customer = null;
        if (dto.getCustomerId() != null) {
            customer = customerRepository.findByIdAndBusinessId(dto.getCustomerId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        }

        // Generate sale number
        int nextNum = saleRepository.findMaxSaleNumber(businessId) + 1;
        String saleNumber = String.format("SAL-%05d", nextNum);

        Sale sale = Sale.builder()
                .saleNumber(saleNumber)
                .customer(customer)
                .business(business)
                .status(Sale.SaleStatus.COMPLETED)
                .paymentMethod(dto.getPaymentMethod() != null ?
                        PaymentMethod.valueOf(dto.getPaymentMethod()) : PaymentMethod.CASH)
                .notes(dto.getNotes())
                .items(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findByIdAndBusinessId(itemDto.getProductId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDto.getProductId()));

            // Validate stock
            Inventory inventory = inventoryRepository.findByProductIdAndBusinessId(product.getId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + product.getName()));

            if (inventory.getCurrentStock() < itemDto.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for " + product.getName() +
                        ". Available: " + inventory.getCurrentStock() +
                        ", Requested: " + itemDto.getQuantity());
            }

            BigDecimal itemTotal = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            SaleItem item = SaleItem.builder()
                    .sale(sale)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice())
                    .totalPrice(itemTotal)
                    .build();
            sale.getItems().add(item);
            totalAmount = totalAmount.add(itemTotal);

            // Decrease inventory
            inventory.setCurrentStock(inventory.getCurrentStock() - itemDto.getQuantity());
            inventoryRepository.save(inventory);

            // Log inventory transaction
            InventoryTransaction txn = InventoryTransaction.builder()
                    .product(product)
                    .type(InventoryTransaction.TransactionType.OUT)
                    .quantity(itemDto.getQuantity())
                    .referenceType("SALE")
                    .notes("Sale to " + (customer != null ? customer.getName() : "Walk-in customer"))
                    .business(business)
                    .build();
            inventoryTransactionRepository.save(txn);
        }

        sale.setTotalAmount(totalAmount);
        BigDecimal paidAmount = dto.getPaidAmount() != null ? dto.getPaidAmount() : totalAmount;
        sale.setPaidAmount(paidAmount);
        sale = saleRepository.save(sale);

        // Create payment record
        Payment payment = Payment.builder()
                .sale(sale)
                .amount(paidAmount)
                .paymentMethod(sale.getPaymentMethod())
                .paymentDate(LocalDate.now())
                .notes("Payment for sale " + saleNumber)
                .business(business)
                .build();
        paymentRepository.save(payment);

        // Create invoice
        int invoiceNum = invoiceRepository.findMaxInvoiceNumber(businessId) + 1;
        Invoice invoice = Invoice.builder()
                .invoiceNumber(String.format("INV-%05d", invoiceNum))
                .sale(sale)
                .totalAmount(totalAmount)
                .status(paidAmount.compareTo(totalAmount) >= 0 ? Invoice.InvoiceStatus.PAID : Invoice.InvoiceStatus.ISSUED)
                .issuedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .business(business)
                .build();
        invoiceRepository.save(invoice);

        return toDto(sale);
    }

    private SaleDto toDto(Sale entity) {
        SaleDto dto = new SaleDto();
        dto.setId(entity.getId());
        dto.setSaleNumber(entity.getSaleNumber());
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
        }
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setOutstandingAmount(entity.getOutstandingAmount());
        dto.setStatus(entity.getStatus().name());
        dto.setPaymentMethod(entity.getPaymentMethod() != null ? entity.getPaymentMethod().name() : null);
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setItems(entity.getItems().stream().map(this::toItemDto).collect(java.util.stream.Collectors.toList()));
        return dto;
    }

    private SaleItemDto toItemDto(SaleItem entity) {
        SaleItemDto dto = new SaleItemDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        return dto;
    }
}
