package com.nashtech.rookie.asset_management_0701.services.returning_request;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import jakarta.persistence.criteria.Join;

public final class ReturningRequestSpecification {

    private ReturningRequestSpecification () {
    }

    public static Specification<ReturningRequest> hasAssetName (String assetName) {
        String lowerCase = assetName == null ? "" : assetName.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> {
            Join<ReturningRequest, Assignment> assignment = root.join("assignment");
            Join<Assignment, Asset> asset = assignment.join("asset");
            return criteriaBuilder.like(criteriaBuilder.lower(asset.get("name")), "%" + lowerCase + "%");
        };
    }

    public static Specification<ReturningRequest> hasAssetCode (String assetCode) {
        String lowerCase = assetCode == null ? "" : assetCode.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> {
            Join<ReturningRequest, Assignment> assignment = root.join("assignment");
            Join<Assignment, Asset> asset = assignment.join("asset");
            return criteriaBuilder.like(criteriaBuilder.lower(asset.get("assetCode")), "%" + lowerCase + "%");
        };
    }

    public static Specification<ReturningRequest> hasRequestUserName (String username){
        String lowerCaseName = username == null ? "" : username.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> {
            Join<Assignment, User> assignmentAsset = root.join("requestedBy");
            return criteriaBuilder.like(criteriaBuilder.lower(assignmentAsset.get("username")), "%"+lowerCaseName+"%");
        };
    }

    public static Specification<ReturningRequest> hasState (Set<EAssignmentReturnState> states) {
        return (root, query, criteriaBuilder) -> {
            if (states == null || states.isEmpty()) {
                return null;
            }
            return root.get("state").in(states);
        };
    }

    public static Specification<ReturningRequest> hasReturnDate (LocalDate returnDate) {
        return (root, query, criteriaBuilder) -> {
            if (returnDate == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("returnDate"), returnDate);
        };
    }

    public static Specification<ReturningRequest> hasLocation (Location location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null){
                return null;
            }
            Join<ReturningRequest, User> returningRequestUser = root.join("requestedBy");
            return criteriaBuilder.equal(returningRequestUser.get("location"), location);
        };
    }
}
