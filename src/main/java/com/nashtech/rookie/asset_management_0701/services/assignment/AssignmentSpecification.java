package com.nashtech.rookie.asset_management_0701.services.assignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.Category;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;

public final class AssignmentSpecification {
    private AssignmentSpecification () {
    }
    public static Specification<Assignment> hasAssetCode (String assetCode) {
        if (assetCode == null || assetCode.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = assetCode.trim().toLowerCase();
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("assetCode")), "%" + lowerCaseName + "%");
    }
    public static Specification<Assignment> hasAssetName (String assetName) {
        if (assetName == null || assetName.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = assetName.trim().toLowerCase();
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerCaseName + "%");
    }
    public static Specification<Assignment> hasCategories (List<Category> categories) {
        return (root, query, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return null;
            }
            return root.get("category").in(categories);
        };
    }
    public static Specification<Assignment> hasAssignedDate (LocalDate assignedDate) {
        return (root, query, criteriaBuilder) -> {
            if (assignedDate == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("assignedDate"), assignedDate);
        };
    }
    public static Specification<Assignment> hasStates (Set<EAssetState> states) {
        return (root, query, criteriaBuilder) -> {
            if (states == null || states.isEmpty()) {
                return null;
            }
            return root.get("state").in(states);
        };
    }
}
