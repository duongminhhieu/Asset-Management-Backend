package com.nashtech.rookie.asset_management_0701.dtos.requests.location;

import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {
    @FieldNotNullConstraint(field = "Location name", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "Location Name", message = "FIELD_NOT_EMPTY")
    private String name;

    @FieldNotNullConstraint(field = "Location code", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "Location code", message = "FIELD_NOT_EMPTY")
    private String code;
}
