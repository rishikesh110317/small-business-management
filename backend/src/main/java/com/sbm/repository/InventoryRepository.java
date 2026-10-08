package com.sbm.repository;

import com.sbm.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);
    Optional<Inventory> findByProductIdAndBusinessId(Long productId, Long businessId);

    @Query("SELECT i FROM Inventory i JOIN FETCH i.product WHERE i.business.id = :businessId")
    List<Inventory> findAllByBusinessIdWithProduct(@Param("businessId") Long businessId);

    Page<Inventory> findByBusinessId(Long businessId, Pageable pageable);

    @Query("SELECT i FROM Inventory i JOIN FETCH i.product WHERE i.business.id = :businessId AND i.currentStock <= i.minStockLevel")
    List<Inventory> findLowStockByBusinessId(@Param("businessId") Long businessId);
}
