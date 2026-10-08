package com.sbm.repository;

import com.sbm.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByBusinessId(Long businessId, Pageable pageable);
    List<Category> findAllByBusinessId(Long businessId);
    Optional<Category> findByIdAndBusinessId(Long id, Long businessId);
    boolean existsByNameAndBusinessId(String name, Long businessId);
}
