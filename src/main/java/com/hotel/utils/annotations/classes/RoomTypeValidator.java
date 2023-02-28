package com.hotel.utils.annotations.classes;

import com.hotel.utils.annotations.ValidateRoomType;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class RoomTypeValidator implements ConstraintValidator<ValidateRoomType,String> {
    @SneakyThrows
    @Override
    public boolean isValid(String t, ConstraintValidatorContext constraintValidatorContext) {
        List<String> roomTypes = Arrays.asList("Jednosobna", "Dvosobna","Trosobna","Četverosobna");

        if (ValidateRoomType.class.getMethod("type").toString().equals("create")) {
            return roomTypes.contains(t);
        }
        return (roomTypes.contains(t) || t == null);
    }
}
