package com.hotel.utils.annotations.classes;

import com.hotel.utils.annotations.ValidateStatus;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class StatusValidator implements ConstraintValidator<ValidateStatus, String> {
    @SneakyThrows
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        List<String> possibleStatus = Arrays.asList("active", "occupied");

        if (ValidateStatus.class.getMethod("type").toString().equals("create")) {
            return possibleStatus.contains(s);
        }
        return (possibleStatus.contains(s) || s == null);
    }
}
