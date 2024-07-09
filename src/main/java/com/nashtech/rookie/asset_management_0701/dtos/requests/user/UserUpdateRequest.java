package com.nashtech.rookie.asset_management_0701.dtos.requests.user;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.enums.EGender;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.validators.dob_constraint.DobConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
import jakarta.validation.constraints.NotNull;
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
public class UserUpdateRequest {
    @DobConstraint(minAge = 18, message = "INVALID_DOB")
    private LocalDate dob;

    @NotNull(message = "INVALID_GENDER")
    private EGender gender;

    @FieldNotNullConstraint(field = "Type", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "Type", message = "FIELD_NOT_EMPTY")
    private ERole type;

    @FieldNotNullConstraint(field = "version", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "version", message = "FIELD_NOT_EMPTY")
    private Long version;

    @NotNull(message = "INVALID_JOIN_DATE")
    private LocalDate joinDate;
}
