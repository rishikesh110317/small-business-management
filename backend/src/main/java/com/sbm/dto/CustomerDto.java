package com.sbm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerDto {
    private Long id;

    @NotBlank(message = "Customer name is required")
    private String name;
    private String email;
    private String phone;
    private String address;
}
