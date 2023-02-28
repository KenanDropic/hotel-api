package com.hotel.utils.dto.Room;

import com.hotel.utils.annotations.ValidateRoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.DecimalMin;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomDto {

    @Nullable
    private Boolean allowedPet;

    @Nullable
    @DecimalMin("0.0")
    private Double currentPrice;

    @Nullable
    @Length(max = 200)
    private String description;

    @ValidateRoomType(type = "update")
    private String roomType;
}

// room id 1 with reservation id 1 with invoice id 1 = 200.0KM