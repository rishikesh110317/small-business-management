package com.sbm.repository;

import com.sbm.entity.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    Page<InventoryTransaction> findByBusinessId(Long businessId, Pageable pageable);
    Page<InventoryTransaction> findByProductIdAndBusinessId(Long productId, Long businessId, Pageable pageable);
}
