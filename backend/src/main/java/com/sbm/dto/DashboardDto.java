package com.sbm.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DashboardDto {
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal totalProfit;
    private BigDecimal outstandingPayments;
    private long totalProducts;
    private long totalCustomers;
    private long totalSuppliers;
    private long totalSales;
    private long totalPurchases;
    private long lowStockItems;
    private java.util.List<MonthlySalesDto> monthlySales;
    private java.util.List<ExpenseByCategoryDto> expensesByCategory;
}
