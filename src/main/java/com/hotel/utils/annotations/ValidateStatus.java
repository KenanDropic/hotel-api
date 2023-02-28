package com.hotel.utils.annotations;

import com.hotel.utils.annotations.classes.StatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = StatusValidator.class)
public @interface ValidateStatus {
    String type();

    String message() default "Please provide one of the following values: active or occupied";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
