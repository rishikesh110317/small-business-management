package com.sbm.controller;

import com.sbm.dto.InvoiceDto;
import com.sbm.entity.Invoice;
import com.sbm.repository.InvoiceRepository;
import com.sbm.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceRepository invoiceRepository;

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAll() {
        Long businessId = SecurityUtils.getCurrentBusinessId();
        List<InvoiceDto> invoices = invoiceRepository.findAllByBusinessIdWithSale(businessId)
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getById(@PathVariable Long id) {
        Long businessId = SecurityUtils.getCurrentBusinessId();
        Invoice invoice = invoiceRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new com.sbm.exception.ResourceNotFoundException("Invoice not found"));
        return ResponseEntity.ok(toDto(invoice));
    }

    private InvoiceDto toDto(Invoice i) {
        InvoiceDto d = new InvoiceDto();
        d.setId(i.getId());
        d.setInvoiceNumber(i.getInvoiceNumber());
        d.setSaleId(i.getSale().getId());
        d.setSaleNumber(i.getSale().getSaleNumber());
        d.setTotalAmount(i.getTotalAmount());
        d.setStatus(i.getStatus().name());
        d.setIssuedDate(i.getIssuedDate());
        d.setDueDate(i.getDueDate());
        d.setCreatedAt(i.getCreatedAt());
        if (i.getSale().getCustomer() != null) {
            d.setCustomerName(i.getSale().getCustomer().getName());
        }
        return d;
    }
}
