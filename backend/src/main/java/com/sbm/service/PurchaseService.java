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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final BusinessRepository businessRepository;

    @Transactional(readOnly = true)
    public Page<PurchaseDto> getAllByBusiness(Long businessId, Pageable pageable) {
        return purchaseRepository.findByBusinessId(businessId, pageable).map(this::toDto);
    }

    public PurchaseDto getById(Long id, Long businessId) {
        Purchase purchase = purchaseRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));
        return toDto(purchase);
    }

    @Transactional
    public PurchaseDto create(PurchaseDto dto, Long businessId, Long userId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        Supplier supplier = supplierRepository.findByIdAndBusinessId(dto.getSupplierId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // Generate purchase number
        int nextNum = purchaseRepository.findMaxPurchaseNumber(businessId) + 1;
        String purchaseNumber = String.format("PUR-%05d", nextNum);

        Purchase purchase = Purchase.builder()
                .purchaseNumber(purchaseNumber)
                .supplier(supplier)
                .business(business)
                .status(Purchase.PurchaseStatus.COMPLETED)
                .notes(dto.getNotes())
                .items(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PurchaseItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findByIdAndBusinessId(itemDto.getProductId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDto.getProductId()));

            BigDecimal itemTotal = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            PurchaseItem item = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice())
                    .totalPrice(itemTotal)
                    .build();
            purchase.getItems().add(item);
            totalAmount = totalAmount.add(itemTotal);

            // Increase inventory
            Inventory inventory = inventoryRepository.findByProductIdAndBusinessId(product.getId(), businessId)
                    .orElseGet(() -> Inventory.builder()
                            .product(product)
                            .currentStock(0)
                            .minStockLevel(10)
                            .business(business)
                            .build());
            inventory.setCurrentStock(inventory.getCurrentStock() + itemDto.getQuantity());
            inventoryRepository.save(inventory);

            // Log inventory transaction
            InventoryTransaction txn = InventoryTransaction.builder()
                    .product(product)
                    .type(InventoryTransaction.TransactionType.IN)
                    .quantity(itemDto.getQuantity())
                    .referenceType("PURCHASE")
                    .referenceId(null) // Will be updated after save
                    .notes("Purchase from " + supplier.getName())
                    .business(business)
                    .build();
            inventoryTransactionRepository.save(txn);
        }

        purchase.setTotalAmount(totalAmount);
        purchase = purchaseRepository.save(purchase);

        // Update supplier outstanding balance
        supplier.setOutstandingBalance(supplier.getOutstandingBalance().add(totalAmount));
        supplierRepository.save(supplier);

        return toDto(purchase);
    }

    private PurchaseDto toDto(Purchase entity) {
        PurchaseDto dto = new PurchaseDto();
        dto.setId(entity.getId());
        dto.setPurchaseNumber(entity.getPurchaseNumber());
        dto.setSupplierId(entity.getSupplier().getId());
        dto.setSupplierName(entity.getSupplier().getName());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setStatus(entity.getStatus().name());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setItems(entity.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
        return dto;
    }

    private PurchaseItemDto toItemDto(PurchaseItem entity) {
        PurchaseItemDto dto = new PurchaseItemDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        return dto;
    }
}
