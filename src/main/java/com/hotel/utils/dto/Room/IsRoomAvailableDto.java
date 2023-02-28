package com.hotel.utils.dto.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsRoomAvailableDto {
    private String checkInDate;
    private String checkOutDate;
}
