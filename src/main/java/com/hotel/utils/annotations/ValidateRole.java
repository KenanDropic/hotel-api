package com.hotel.utils.annotations;

import com.hotel.utils.annotations.classes.RoleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = RoleValidator.class)
public @interface ValidateRole {
    String message() default "Please provide one of the following roles: ROLE_ADMIN,ROLE_EMPLOYEE,ROLE_GUEST";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
