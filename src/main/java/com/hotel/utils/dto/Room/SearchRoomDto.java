package com.hotel.utils.dto.Room;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class SearchRoomDto {
    @Nullable
    private Boolean allowedPet;

    @Nullable
    private String type;

    @Nullable
    private Double price;

    private String comparisonOperator = "greater";
    private String field = "room_id";
    private String direction = "ASC";

}
