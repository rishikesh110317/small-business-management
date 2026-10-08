package com.sbm.repository;

import com.sbm.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Business> findByIsActive(Boolean isActive, Pageable pageable);
}
