package com.nashtech.rookie.asset_management_0701.dtos.requests.asset;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.validators.easset_subset.EAssetStateDeserializer;
import com.nashtech.rookie.asset_management_0701.validators.easset_subset.EAssetStateSubset;
import com.nashtech.rookie.asset_management_0701.validators.field_not_empty.FieldNotEmptyConstraint;
import com.nashtech.rookie.asset_management_0701.validators.field_not_null.FieldNotNullConstraint;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class AssetCreateDto {

    @FieldNotNullConstraint(field = "name", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "name", message = "FIELD_NOT_EMPTY")
    @Size(max = 255)
    String name;

    @FieldNotNullConstraint(field = "specification", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "specification", message = "FIELD_NOT_EMPTY")
    @Size(max = 1024)
    String specification;

    @FieldNotNullConstraint(field = "name", message = "FIELD_NOT_NULL")
    LocalDateTime installDate;

    @FieldNotNullConstraint(field = "state", message = "FIELD_NOT_NULL")
    @EAssetStateSubset(
            anyOf = {EAssetState.AVAILABLE, EAssetState.NOT_AVAILABLE},
            message = "STATE_NOT_AVAILABLE")
    @Enumerated(EnumType.STRING)
    @JsonDeserialize(using = EAssetStateDeserializer.class)
    EAssetState state;

    @FieldNotNullConstraint(field = "category", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "category", message = "FIELD_NOT_EMPTY")
    String category;
}
