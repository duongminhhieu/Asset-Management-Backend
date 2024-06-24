package com.nashtech.rookie.asset_management_0701.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.rookie.asset_management_0701.entities.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByName (String name);
    boolean existsByCode (String code);
}
