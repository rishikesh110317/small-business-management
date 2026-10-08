package com.sbm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SupplierDto {
    private Long id;

    @NotBlank(message = "Supplier name is required")
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private BigDecimal outstandingBalance;
}
