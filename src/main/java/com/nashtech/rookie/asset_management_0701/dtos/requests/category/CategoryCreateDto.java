package com.nashtech.rookie.asset_management_0701.dtos.requests.category;

import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
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
public class CategoryCreateDto {

    @FieldNotNullConstraint(field = "name", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "name", message = "FIELD_NOT_EMPTY")
    String name;

    @FieldNotNullConstraint(field = "code", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "code", message = "FIELD_NOT_EMPTY")
    @Size(min = 2, max = 2, message = "INVALID_CATEGORY_CODE")
    String code;
}
