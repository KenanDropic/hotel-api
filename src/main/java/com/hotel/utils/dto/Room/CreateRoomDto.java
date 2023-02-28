package com.hotel.utils.dto.Room;

import com.hotel.utils.annotations.ValidateRoomType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateRoomDto {
    @NotNull(message = "Field cannot be null")
    private Integer room_number;
    @NotNull(message = "Field cannot be null")
    @NotEmpty(message = "Field cannot be empty")
    private String description;
    @ValidateRoomType(type = "create")
    private String room_type;
    @NotNull(message = "Field cannot be null")
    private Boolean allowed_pet;
    @NotNull(message = "Field cannot be null")
    private Double price;
}
