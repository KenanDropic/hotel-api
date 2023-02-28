package com.hotel.utils;

import com.hotel.exception.exceptions.BadRequestExc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class UpdateBodyValidation<T> {
    public void checkRequestBody(List<T> existingValues, List<Object> passedValues) {
        List<Boolean> equality = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        // check if all values are null. If so then throw bad request exception
        boolean areAllNull = passedValues.stream().allMatch(Objects::isNull);

        if (areAllNull) {
            throw new BadRequestExc("Please provide values for update. Object cannot be empty!");
        }

        // checking if stored value and passed value are equal
        for (int i = 0, n = existingValues.size(); i < n; i++) {
            if (passedValues.get(i) == null) {
                equality.add(false);
            } else if (passedValues.get(i).equals(existingValues.get(i))) {
                equality.add(passedValues.get(i).equals(existingValues.get(i)));
                values.add(existingValues.get(i));
            } else {
                equality.add(false);
            }
        }

        if (equality.contains(true)) {
            String verb = values.size() <= 1 ? " is" : " are";
            String noun = values.size() <= 1 ? "one" : "ones";
            throw new BadRequestExc("When updating please provide new values. " +
                    values + verb + " same as the " + noun + " in database");
        }
    }

}
