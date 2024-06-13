package com.nashtech.rookie.asset_management_0701.validators.DobConstraint;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "Invalid Date of Birth";
    int minAge();
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
