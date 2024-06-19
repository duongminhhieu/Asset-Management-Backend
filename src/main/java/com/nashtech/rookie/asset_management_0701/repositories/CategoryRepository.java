package com.nashtech.rookie.asset_management_0701.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.rookie.asset_management_0701.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName (String categoryName);

    Optional<Category> findByCode (String categoryCode);
}
