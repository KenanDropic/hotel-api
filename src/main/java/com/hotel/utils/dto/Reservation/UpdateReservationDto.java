package com.hotel.utils.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationDto {
    @Nullable
    private LocalDateTime checkIn;

    @Nullable
    private LocalDateTime checkOut;
    // when testing the app is finished add annotations @PresentOrFuture for both fields
}
