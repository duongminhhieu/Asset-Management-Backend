package com.nashtech.rookie.asset_management_0701.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.rookie.asset_management_0701.entities.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Long countByAssetCodeStartingWith (String code);
}
