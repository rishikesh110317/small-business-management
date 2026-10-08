package com.sbm.repository;

import com.sbm.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByBusinessId(Long businessId, Pageable pageable);
    Optional<Customer> findByIdAndBusinessId(Long id, Long businessId);
    long countByBusinessId(Long businessId);

    @Query("SELECT c FROM Customer c WHERE c.business.id = :businessId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR c.phone LIKE CONCAT('%', :search, '%'))")
    Page<Customer> searchByBusinessId(@Param("businessId") Long businessId, @Param("search") String search, Pageable pageable);
}
