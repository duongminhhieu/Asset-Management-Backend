package com.nashtech.rookie.asset_management_0701.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nashtech.rookie.asset_management_0701.entities.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {
    Long countByAssetCodeStartingWith (String code);
}
