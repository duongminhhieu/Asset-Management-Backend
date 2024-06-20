package com.nashtech.rookie.asset_management_0701.dtos.requests.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AuthenticationRequest {

    @FieldNotEmptyConstraint(field = "username", message = "FILED_NOT_EMPTY")
    @FieldNotNullConstraint(field = "username", message = "FILED_NOT_NULL")
    private String username;

    @FieldNotNullConstraint(field = "password", message = "FILED_NOT_NULL")
    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;
}
