package com.nashtech.rookie.asset_management_0701.validators.easset_subset;

import java.util.Arrays;

import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EAssetStateSubsetValidator implements ConstraintValidator<EAssetStateSubset, EAssetState> {
    private EAssetState[] subset;

    @Override
    public void initialize (EAssetStateSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid (EAssetState value, ConstraintValidatorContext context) {
        return Arrays.asList(subset).contains(value);
    }
}
