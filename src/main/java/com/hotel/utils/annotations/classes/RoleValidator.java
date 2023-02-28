package com.hotel.utils.annotations.classes;

import com.hotel.utils.annotations.ValidateRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class RoleValidator implements ConstraintValidator<ValidateRole, String> {
    @Override
    public boolean isValid(String r, ConstraintValidatorContext constraintValidatorContext) {
        List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_GUEST");

        return roles.contains(r);
    }
}
