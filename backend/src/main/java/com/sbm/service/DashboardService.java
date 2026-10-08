package com.sbm.service;

import com.sbm.dto.*;
import com.sbm.entity.*;
import com.sbm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final ExpenseRepository expenseRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;

    public DashboardDto getDashboard(Long businessId) {
        BigDecimal totalRevenue = saleRepository.sumTotalByBusinessId(businessId);
        BigDecimal totalExpenses = expenseRepository.sumTotalByBusinessId(businessId);
        BigDecimal totalPurchaseCost = purchaseRepository.sumTotalByBusinessId(businessId);
        BigDecimal totalProfit = totalRevenue.subtract(totalExpenses).subtract(totalPurchaseCost);
        BigDecimal outstanding = saleRepository.sumOutstandingByBusinessId(businessId);

        long totalProducts = productRepository.countByBusinessId(businessId);
        long totalCustomers = customerRepository.countByBusinessId(businessId);
        long totalSuppliers = supplierRepository.countByBusinessId(businessId);
        long totalSales = saleRepository.countByBusinessId(businessId);
        long totalPurchases = purchaseRepository.countByBusinessId(businessId);
        long lowStockItems = inventoryRepository.findLowStockByBusinessId(businessId).size();

        // Monthly sales for last 6 months
        List<MonthlySalesDto> monthlySales = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            LocalDateTime start = ym.atDay(1).atStartOfDay();
            LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);
            BigDecimal amount = saleRepository.sumTotalByBusinessIdAndDateRange(businessId, start, end);
            long count = saleRepository.countByBusinessIdAndDateRange(businessId, start, end);
            monthlySales.add(new MonthlySalesDto(ym.getMonth().name().substring(0, 3), amount, count));
        }

        // Expenses by category for current month
        LocalDate monthStart = YearMonth.now().atDay(1);
        LocalDate monthEnd = YearMonth.now().atEndOfMonth();
        List<Object[]> expByCat = expenseRepository.sumByCategoryAndBusinessIdAndDateRange(businessId, monthStart, monthEnd);
        List<ExpenseByCategoryDto> expensesByCategory = new ArrayList<>();
        for (Object[] row : expByCat) {
            expensesByCategory.add(new ExpenseByCategoryDto(row[0].toString(), (BigDecimal) row[1]));
        }

        return DashboardDto.builder()
                .totalRevenue(totalRevenue).totalExpenses(totalExpenses).totalProfit(totalProfit)
                .outstandingPayments(outstanding).totalProducts(totalProducts)
                .totalCustomers(totalCustomers).totalSuppliers(totalSuppliers)
                .totalSales(totalSales).totalPurchases(totalPurchases)
                .lowStockItems(lowStockItems).monthlySales(monthlySales)
                .expensesByCategory(expensesByCategory)
                .build();
    }
}
