package com.sbm.repository;

import com.sbm.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByBusinessId(Long businessId, Pageable pageable);
    Optional<Invoice> findByIdAndBusinessId(Long id, Long businessId);
    Optional<Invoice> findBySaleIdAndBusinessId(Long saleId, Long businessId);

    @Query("SELECT i FROM Invoice i JOIN FETCH i.sale s LEFT JOIN FETCH s.customer WHERE i.business.id = :businessId ORDER BY i.createdAt DESC")
    List<Invoice> findAllByBusinessIdWithSale(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(i.invoiceNumber, 5) AS int)), 0) FROM Invoice i WHERE i.business.id = :businessId")
    int findMaxInvoiceNumber(@Param("businessId") Long businessId);
}
