package com.sbm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlySalesDto {
    private String month;
    private BigDecimal amount;
    private long count;
}
