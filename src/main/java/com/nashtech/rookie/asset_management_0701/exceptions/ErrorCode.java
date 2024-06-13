package com.nashtech.rookie.asset_management_0701.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

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


    // constraint violation
    INVALID_PASSWORD(2001, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST);



    private final int internalCode;
    private final String message;
    private final HttpStatusCode statusCode;
}
