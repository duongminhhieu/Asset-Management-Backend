package com.nashtech.rookie.asset_management_0701.dtos.requests.user;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.enums.EGender;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.validators.dob_constraint.DobConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserRequest {

    @FieldNotNullConstraint(field = "firstName", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "firstName", message = "FIELD_NOT_EMPTY")
    @Size(max = 128, message = "EXCEED_MAX_FIRSTNAME")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "INVALID_FIRSTNAME")
    private String firstName;

    @FieldNotNullConstraint(field = "lastName", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "lastName", message = "FIELD_NOT_EMPTY")
    @Size(max = 128, message = "EXCEED_MAX_LASTNAME")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "INVALID_LASTNAME")
    private String lastName;

    @DobConstraint(minAge = 18, message = "INVALID_DOB")
    private LocalDate dob;

    @NotNull(message = "INVALID_GENDER")
    private EGender gender;

    @NotNull(message = "INVALID_JOIN_DATE")
    private LocalDate joinDate;

    private ERole role;

    private Long locationId;
}
