package com.nashtech.rookie.asset_management_0701.validators.field_not_empty;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldNotEmptyValidator.class)
public @interface FieldNotEmptyConstraint {
    String message();

    String field();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
