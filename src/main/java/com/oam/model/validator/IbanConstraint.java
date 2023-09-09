package com.oam.model.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IbanValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IbanConstraint {
    String message() default "Invalid iban";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
