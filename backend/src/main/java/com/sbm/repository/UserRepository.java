package com.sbm.repository;

import com.sbm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User> findByBusinessId(Long businessId, Pageable pageable);
    Page<User> findByBusinessIdAndRoleId(Long businessId, Long roleId, Pageable pageable);
    long countByBusinessId(Long businessId);
}
