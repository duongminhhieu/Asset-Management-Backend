package com.nashtech.rookie.asset_management_0701.dtos.requests.user;

import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
import com.nashtech.rookie.asset_management_0701.validators.password_constraint.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirstChangePasswordRequest {

    @FieldNotNullConstraint(field = "password", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "password", message = "FIELD_NOT_EMPTY")
    @PasswordConstraint(message = "INVALID_PASSWORD")
    private String password;
}
