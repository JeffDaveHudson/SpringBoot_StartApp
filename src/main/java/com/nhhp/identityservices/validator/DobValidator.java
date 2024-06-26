package com.nhhp.identityservices.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private  int min;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(value))
            return true;
        long years = ChronoUnit.YEARS.between(value, LocalDate.now());
        return years >= min;
    }

    // get thong tin tu doi tuong cua DobConstraint\
    // chay truoc method isValid
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
