package com.nashtech.rookie.asset_management_0701.validators.field_not_empty;

import java.util.Collection;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldNotEmptyValidator implements ConstraintValidator<FieldNotEmptyConstraint, Object> {

    @Override
    public void initialize (FieldNotEmptyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid (Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)){
            return true;
        }
        return switch (value) {
            case String s -> !s.trim().isEmpty();
            case Collection<?> collection -> !collection.isEmpty();
            case Double v -> !v.isNaN() && !v.isInfinite();
            case Float v -> !v.isNaN() && !v.isInfinite();
            default -> true;
        };
    }
}
