package com.nashtech.rookie.asset_management_0701.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_TOKEN(1001, "Invalid token", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(1002, "Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1003, "You do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),
    EMAIL_OR_PASSWORD_INCORRECT(1006, "Username or password is incorrect", HttpStatus.BAD_REQUEST),
    USER_NOT_ACTIVE(1007, "User is not active", HttpStatus.BAD_REQUEST),
    INVALID_PAGEABLE(1008, "Invalid pageable", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_CHANGE(1009, "Password is not change first time", HttpStatus.BAD_REQUEST),
    INVALID_SORT_DIR(1010, "Sort direction must be ASC or DESC", HttpStatus.BAD_REQUEST),
    INVALID_SORT_FIELD(1011, "Invalid sort field", HttpStatus.BAD_REQUEST),


    // User
    EXCEED_MAX_FIRSTNAME(1100, "First name must not exceed {max} characters", HttpStatus.BAD_REQUEST),
    INVALID_FIRSTNAME(1101, "First name must not contain special characters", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_LASTNAME(1102, "Last name must not exceed {max} characters", HttpStatus.BAD_REQUEST),
    INVALID_LASTNAME(1103, "Last name must not contain special characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1104, "User is under {minAge}. Please select a different date", HttpStatus.BAD_REQUEST),
    INVALID_GENDER(1105, "Gender is required", HttpStatus.BAD_REQUEST),
    INVALID_JOIN_DATE(1106, "Joined date is required", HttpStatus.BAD_REQUEST),
    JOIN_DATE_BEFORE_DOB(
            1107,
            "Joined date is not later than Date of Birth. Please select a different date",
            HttpStatus.BAD_REQUEST),
    JOIN_DATE_WEEKEND(
            1108, "Joined date is Saturday or Sunday. Please select a different date", HttpStatus.BAD_REQUEST),
    INVALID_ROLE(1109, "Type is required", HttpStatus.BAD_REQUEST),
    ADMIN_NULL_LOCATION(1110, "Type admin must have location", HttpStatus.BAD_REQUEST),
    PASSWORD_CHANGED(1111, "Password is already changed first time", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1112, "The current password is incorrect", HttpStatus.BAD_REQUEST),
    PASSWORD_SAME(1113, "The new password must be different from the current password", HttpStatus.BAD_REQUEST),
    LOCATION_NAME_ALREADY_EXISTED(1114, "Location name is already exist", HttpStatus.BAD_REQUEST),
    LOCATION_CODE_ALREADY_EXISTED(1115, "Location code is already exist", HttpStatus.BAD_REQUEST),

    // Category
    CATEGORY_NAME_ALREADY_EXISTED(1201, "Category is already existed. Please enter a different category"
            , HttpStatus.BAD_REQUEST),
    CATEGORY_PREFIX_ALREADY_EXISTED(1202, "Prefix is already existed. Please enter a different prefix"
            , HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1203, "Category not found", HttpStatus.NOT_FOUND),
    // Asset
    ASSET_INSTALLED_DATE_TOO_OLD(1301, "Asset installed date is no longer than 3 months ago", HttpStatus.BAD_REQUEST),
    ASSET_NOT_FOUND(1302, "Asset not found", HttpStatus.NOT_FOUND),
    // constraint violation
    INVALID_PASSWORD(2001, "Password must be at least 8 characters less than 128 characters " +
        "contains at least 1 uppercase, " +
        "1 lowercase, 1 special characters, 1 number", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_CODE(2202, "Category must be {min} characters", HttpStatus.BAD_REQUEST),
    FIELD_NOT_EMPTY(2003, "\"{field}\" must not be empty", HttpStatus.BAD_REQUEST),
    FIELD_NOT_NULL(2004, "\"{field}\" must not be null", HttpStatus.BAD_REQUEST),
    STATE_NOT_AVAILABLE(2205, "Status must be any of AVAILABLE, NOT_AVAILABLE", HttpStatus.BAD_REQUEST),
    PAGE_SIZE_LESS_THAN_ONE(2005, "Page size must be larger than 0", HttpStatus.BAD_REQUEST),
    PAGE_NUMBER_LESS_THAN_ONE(2006, "Page number must be larger than 0", HttpStatus.BAD_REQUEST),
    ROLE_NOT_AVAILABLE(2100, "This is not a type of user for this system", HttpStatus.BAD_REQUEST);

    private final int internalCode;
    private final String message;
    private final HttpStatusCode statusCode;
}
