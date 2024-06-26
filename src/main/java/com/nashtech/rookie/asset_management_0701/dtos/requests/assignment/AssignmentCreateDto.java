package com.nashtech.rookie.asset_management_0701.dtos.requests.assignment;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentCreateDto {

    @FieldNotNullConstraint(field = "user", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "user", message = "FIELD_NOT_EMPTY")
    Long userId;

    @FieldNotNullConstraint(field = "asset", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "asset", message = "FIELD_NOT_EMPTY")
    Long assetId;

    @FutureOrPresent(message = "ASSIGNMENT_ASSIGNED_DATE_INVALID")
    LocalDate assignedDate;

    @FieldNotNullConstraint(field = "note", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "note", message = "FIELD_NOT_EMPTY")
    @Size(max = 1024)
    String note;
}
