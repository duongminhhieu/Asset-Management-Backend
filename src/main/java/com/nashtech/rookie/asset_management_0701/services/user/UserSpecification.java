package com.nashtech.rookie.asset_management_0701.services.user;

import org.springframework.data.jpa.domain.Specification;

import com.nashtech.rookie.asset_management_0701.constants.DefaultSortOptions;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;

public final class UserSpecification {
    private UserSpecification () {
    }

    public static Specification<User> hasNameContains (String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = name.trim().toLowerCase();
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(
                    criteriaBuilder.concat(criteriaBuilder.concat(root.get("firstName"), " "), root.get("lastName"))),
                    "%" + lowerCaseName + "%");
    }

    public static Specification<User> hasStaffCodeContains (String staffCode) {
        if (staffCode == null || staffCode.trim().isEmpty()) {
            return null;
        }
        String lowerCaseName = staffCode.trim().toLowerCase();
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("staffCode")), "%" + lowerCaseName + "%");
    }

    public static Specification<User> hasRole (String role) {
        if (role == null || role.trim().isEmpty()) {
            return null;
        }

        String upperCaseRole = role.trim().toUpperCase();
        if (!DefaultSortOptions.SYSTEM_ROLE.contains(upperCaseRole)){
            throw new AppException(ErrorCode.ROLE_NOT_AVAILABLE);
        }

        if (upperCaseRole.equals("STAFF")) {
            upperCaseRole = "USER";
        }

        ERole eRole = ERole.valueOf(upperCaseRole);
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("role"), eRole);
        };
    }

    public static Specification<User> usernameStartsWith (String baseUsername) {
        return (root, query, builder) -> builder.like(root.get("username"), baseUsername + "%");
    }

    public static Specification<User> hasLocation (Location location) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("location"), location);
    }

    public static Specification<User> excludeUser (User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root, user);
    }
}
