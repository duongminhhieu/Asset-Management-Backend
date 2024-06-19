package com.nashtech.rookie.asset_management_0701.repositories;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nashtech.rookie.asset_management_0701.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername (String userName);

    boolean existsByUsername (String username);

    long count (Specification<User> spec);
}
