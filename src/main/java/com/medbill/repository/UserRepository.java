package com.medbill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medbill.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // ================= LOGIN =================

    Optional<User> findByEmail(String email);

    // ================= DUPLICATE EMAIL CHECK =================

    boolean existsByEmail(String email);

    // ================= TENANT / COMPANY =================

    List<User> findByCompanyId(Long companyId);

    // ================= TENANT USER COUNT =================

    long countByCompanyId(Long companyId);
}