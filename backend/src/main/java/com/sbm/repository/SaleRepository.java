package com.sbm.repository;

import com.sbm.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Page<Sale> findByBusinessId(Long businessId, Pageable pageable);
    Optional<Sale> findByIdAndBusinessId(Long id, Long businessId);
    long countByBusinessId(Long businessId);
    Page<Sale> findByBusinessIdAndCustomerId(Long businessId, Long customerId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.business.id = :businessId AND s.status = 'COMPLETED'")
    BigDecimal sumTotalByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.business.id = :businessId AND s.status = 'COMPLETED' AND s.createdAt BETWEEN :start AND :end")
    BigDecimal sumTotalByBusinessIdAndDateRange(@Param("businessId") Long businessId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.paidAmount), 0) FROM Sale s WHERE s.business.id = :businessId AND s.status = 'COMPLETED'")
    BigDecimal sumPaidByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(s.totalAmount - s.paidAmount), 0) FROM Sale s WHERE s.business.id = :businessId AND s.status = 'COMPLETED' AND s.totalAmount > s.paidAmount")
    BigDecimal sumOutstandingByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(s.saleNumber, 5) AS int)), 0) FROM Sale s WHERE s.business.id = :businessId")
    int findMaxSaleNumber(@Param("businessId") Long businessId);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.business.id = :businessId AND s.createdAt BETWEEN :start AND :end")
    long countByBusinessIdAndDateRange(@Param("businessId") Long businessId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
