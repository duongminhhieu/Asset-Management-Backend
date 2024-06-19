package com.nashtech.rookie.asset_management_0701.validators.password_constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordConstraint {

    String message();

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
