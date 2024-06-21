package com.nashtech.rookie.asset_management_0701.validators.password_constraint;

import jakarta.validation.ConstraintValidator;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public void initialize (PasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid (String password, jakarta.validation.ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,128}$";

        return password.matches(regex);
    }
}
