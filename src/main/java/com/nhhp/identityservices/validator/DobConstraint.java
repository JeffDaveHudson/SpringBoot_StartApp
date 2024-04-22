package com.nhhp.identityservices.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class}) // tao lop xu ly validation.
public @interface DobConstraint { // khai bao validation chu chua xu ly, ta can tao lop xu ly validation.

    String message() default "Invalid date of birth";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min();
}
