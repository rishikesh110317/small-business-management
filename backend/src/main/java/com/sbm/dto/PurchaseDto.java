package com.sbm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseDto {
    private Long id;
    private String purchaseNumber;

    @NotNull(message = "Supplier is required")
    private Long supplierId;
    private String supplierName;

    private BigDecimal totalAmount;
    private String status;
    private String notes;
    private LocalDateTime createdAt;

    @NotEmpty(message = "Purchase must have at least one item")
    private List<PurchaseItemDto> items;
}
