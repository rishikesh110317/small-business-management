package com.sbm.repository;

import com.sbm.entity.Purchase;
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
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Page<Purchase> findByBusinessId(Long businessId, Pageable pageable);
    Optional<Purchase> findByIdAndBusinessId(Long id, Long businessId);
    long countByBusinessId(Long businessId);

    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Purchase p WHERE p.business.id = :businessId AND p.status = 'COMPLETED'")
    BigDecimal sumTotalByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Purchase p WHERE p.business.id = :businessId AND p.status = 'COMPLETED' AND p.createdAt BETWEEN :start AND :end")
    BigDecimal sumTotalByBusinessIdAndDateRange(@Param("businessId") Long businessId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(p.purchaseNumber, 5) AS int)), 0) FROM Purchase p WHERE p.business.id = :businessId")
    int findMaxPurchaseNumber(@Param("businessId") Long businessId);
}
