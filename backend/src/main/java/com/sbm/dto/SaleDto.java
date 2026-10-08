package com.sbm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDto {
    private Long id;
    private String saleNumber;

    private Long customerId;
    private String customerName;

    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal outstandingAmount;
    private String status;
    private String paymentMethod;
    private String notes;
    private LocalDateTime createdAt;

    @NotEmpty(message = "Sale must have at least one item")
    private List<SaleItemDto> items;
}
