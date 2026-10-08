package com.sbm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExpenseByCategoryDto {
    private String category;
    private BigDecimal amount;
}
