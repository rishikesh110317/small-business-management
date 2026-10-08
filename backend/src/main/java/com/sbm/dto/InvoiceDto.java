package com.sbm.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InvoiceDto {
    private Long id;
    private String invoiceNumber;
    private Long saleId;
    private String saleNumber;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate issuedDate;
    private LocalDate dueDate;
    private String customerName;
    private LocalDateTime createdAt;
}
