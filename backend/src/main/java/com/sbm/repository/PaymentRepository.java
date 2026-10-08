package com.sbm.repository;

import com.sbm.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByBusinessId(Long businessId, Pageable pageable);
    List<Payment> findBySaleIdAndBusinessId(Long saleId, Long businessId);

    @Query("SELECT p FROM Payment p LEFT JOIN FETCH p.sale s LEFT JOIN FETCH s.customer WHERE p.business.id = :businessId ORDER BY p.paymentDate DESC")
    List<Payment> findAllByBusinessIdWithSale(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.business.id = :businessId")
    BigDecimal sumTotalByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.business.id = :businessId AND p.paymentDate BETWEEN :start AND :end")
    BigDecimal sumTotalByBusinessIdAndDateRange(@Param("businessId") Long businessId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
