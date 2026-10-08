package com.sbm.controller;

import com.sbm.dto.PaymentDto;
import com.sbm.entity.*;
import com.sbm.exception.ResourceNotFoundException;
import com.sbm.repository.*;
import com.sbm.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;
    private final BusinessRepository businessRepository;

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAll() {
        Long businessId = SecurityUtils.getCurrentBusinessId();
        List<PaymentDto> payments = paymentRepository.findAllByBusinessIdWithSale(businessId)
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody PaymentDto dto) {
        Long businessId = SecurityUtils.getCurrentBusinessId();
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        Sale sale = null;
        if (dto.getSaleId() != null) {
            sale = saleRepository.findByIdAndBusinessId(dto.getSaleId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        }

        Payment payment = Payment.builder()
                .sale(sale)
                .amount(dto.getAmount())
                .paymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()))
                .paymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDate.now())
                .notes(dto.getNotes())
                .business(business)
                .build();
        payment = paymentRepository.save(payment);

        // Update sale paid amount
        if (sale != null) {
            sale.setPaidAmount(sale.getPaidAmount().add(dto.getAmount()));
            saleRepository.save(sale);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(payment));
    }

    private PaymentDto toDto(Payment p) {
        PaymentDto d = new PaymentDto();
        d.setId(p.getId());
        d.setAmount(p.getAmount());
        d.setPaymentMethod(p.getPaymentMethod().name());
        d.setPaymentDate(p.getPaymentDate());
        d.setNotes(p.getNotes());
        if (p.getSale() != null) {
            d.setSaleId(p.getSale().getId());
            d.setSaleNumber(p.getSale().getSaleNumber());
            if (p.getSale().getCustomer() != null) {
                d.setCustomerName(p.getSale().getCustomer().getName());
            }
        }
        return d;
    }
}
