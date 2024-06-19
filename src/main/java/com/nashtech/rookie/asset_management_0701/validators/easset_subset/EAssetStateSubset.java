package com.nashtech.rookie.asset_management_0701.validators.easset_subset;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EAssetStateSubsetValidator.class)
public @interface EAssetStateSubset {

    String message() default "Invalid asset state";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    EAssetState[] anyOf();
}
