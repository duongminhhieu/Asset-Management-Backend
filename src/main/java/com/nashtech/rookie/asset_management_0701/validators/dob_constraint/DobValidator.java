package com.nashtech.rookie.asset_management_0701.validators.dob_constraint;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int minAge;

    @Override
    public void initialize (DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minAge = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid (LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) {
            return true;
        }
        long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        return years >= minAge;
    }
}
