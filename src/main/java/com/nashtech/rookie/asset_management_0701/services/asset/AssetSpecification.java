package com.nashtech.rookie.asset_management_0701.services.asset;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Category;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;


public final class AssetSpecification {

    private AssetSpecification () {
    }
    public static Specification<Asset> hasAssetName (String assetName) {
        if (assetName == null || assetName.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = assetName.trim().toLowerCase();
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerCaseName + "%");
    }
    public static Specification<Asset> hasAssetCode (String assetCode) {
        if (assetCode == null || assetCode.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = assetCode.trim().toLowerCase();
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("assetCode")), "%" + lowerCaseName + "%");
    }
    public static Specification<Asset> hasStates (Set<EAssetState> states) {
        return (root, query, criteriaBuilder) -> {
            if (states == null || states.isEmpty()) {
                return null;
            }
            return root.get("state").in(states);
        };
    }
    public static Specification<Asset> hasCategories (List<Category> categories) {
        return (root, query, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return null;
            }
            return root.get("category").in(categories);
        };
    }
    public static Specification<Asset> hasLocation (Location location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("location"), location);
        };
    }
}
