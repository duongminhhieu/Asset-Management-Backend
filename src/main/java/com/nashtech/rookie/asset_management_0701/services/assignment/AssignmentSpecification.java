package com.nashtech.rookie.asset_management_0701.services.assignment;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import jakarta.persistence.criteria.Join;
public final class AssignmentSpecification {
    private AssignmentSpecification (){
    }

    public static Specification<Assignment> hasStates (Set<EAssignmentState> states) {
        return (root, query, criteriaBuilder) -> {
            if (states == null || states.isEmpty()) {
                return null;
            }
            return root.get("state").in(states);
        };
    }

    public static Specification<Assignment> hasLocation (Location location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null){
                return null;
            }
            Join<Assignment, Asset> assignmentAsset = root.join("asset");
            return criteriaBuilder.equal(assignmentAsset.get("location"), location);
        };
    }

    public static Specification<Assignment> hasAssetName (String assetName){
        String lowerCaseName = assetName == null? "":assetName.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> {
            Join<Assignment, Asset> assignmentAsset = root.join("asset");
            return criteriaBuilder.like(criteriaBuilder.lower(assignmentAsset.get("name")), "%"+lowerCaseName+"%");
        };
    }

    public static Specification<Assignment> hasAssetCode (String assetCode){
        String lowerCaseName = assetCode == null? "": assetCode.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> {
            Join<Assignment, Asset> assignmentAsset = root.join("asset");
            return criteriaBuilder.like(criteriaBuilder.lower(assignmentAsset.get("assetCode")), "%"+lowerCaseName+"%");
        };
    }

    public static Specification<Assignment> hasAssigneeUsername (String username){
        String lowerCaseName = username == null? "":username.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> {
            Join<Assignment, User> assignmentAsset = root.join("assignTo");
            return criteriaBuilder.like(criteriaBuilder.lower(assignmentAsset.get("username")), "%"+lowerCaseName+"%");
        };
    }

    public static Specification<Assignment> assignOnDate (LocalDate assignDate){
        return (root, query, criteriaBuilder) -> {
            if (assignDate == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("assignedDate"), assignDate);
        };
    }

    public static Specification<Assignment> notStateReturned (){
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("state"), EAssignmentState.RETURNED);
    }

    public static Specification<Assignment> notStateDeclined (){
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("state"), EAssignmentState.DECLINED);
    }

    public static Specification<Assignment> assignToIdEquals (Long assignToId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("assignTo").get("id"), assignToId);
    }

    public static Specification<Assignment> assignedDateLessThanEqual (LocalDate assignedDate) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("assignedDate"), assignedDate);
    }
}
