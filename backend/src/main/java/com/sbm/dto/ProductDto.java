package com.sbm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;
    private String sku;

    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.0", message = "Cost price must be non-negative")
    private BigDecimal costPrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", message = "Selling price must be non-negative")
    private BigDecimal sellingPrice;

    private Long categoryId;
    private String categoryName;
    private Boolean isActive;
    private Integer currentStock;
    private Integer minStockLevel;
}
