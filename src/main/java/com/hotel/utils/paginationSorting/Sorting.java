package com.hotel.utils.paginationSorting;

import com.hotel.exception.exceptions.BadRequestExc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class Sorting {

    List<String> directions = Arrays.asList("ASC", "DESC");

    public void containsField(List<String> fieldList, String field) {
        if (!fieldList.contains(field)) {
            throw new BadRequestExc("Sorting is allowed only by fields: " + fieldList);
        }
    }

    public void containsDirection(String direction) {
        if (!directions.contains(direction)) {
            throw new BadRequestExc("Sorting is possible only by two directions: ASC or DESC");
        }
    }

}
