package com.sbm.repository;

import com.sbm.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByBusinessId(Long businessId, Pageable pageable);
    Page<Expense> findByBusinessIdAndCategory(Long businessId, Expense.ExpenseCategory category, Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.business.id = :businessId")
    BigDecimal sumTotalByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.business.id = :businessId AND e.expenseDate BETWEEN :start AND :end")
    BigDecimal sumTotalByBusinessIdAndDateRange(@Param("businessId") Long businessId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT e.category, COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.business.id = :businessId AND e.expenseDate BETWEEN :start AND :end GROUP BY e.category")
    java.util.List<Object[]> sumByCategoryAndBusinessIdAndDateRange(@Param("businessId") Long businessId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
