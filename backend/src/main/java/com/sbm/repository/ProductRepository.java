package com.sbm.repository;

import com.sbm.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.business.id = :businessId")
    List<Product> findAllByBusinessIdWithCategory(@Param("businessId") Long businessId);

    Page<Product> findByBusinessId(Long businessId, Pageable pageable);
    Page<Product> findByBusinessIdAndIsActive(Long businessId, Boolean isActive, Pageable pageable);
    Page<Product> findByBusinessIdAndCategoryId(Long businessId, Long categoryId, Pageable pageable);
    Optional<Product> findByIdAndBusinessId(Long id, Long businessId);
    boolean existsBySkuAndBusinessId(String sku, Long businessId);
    long countByBusinessId(Long businessId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.business.id = :businessId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Product> searchByBusinessIdWithCategory(@Param("businessId") Long businessId, @Param("search") String search);

    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchByBusinessId(@Param("businessId") Long businessId, @Param("search") String search, Pageable pageable);
}
