package com.hotel.utils.dto.Reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateReservationDto {
    // when testing is finished uncomment @FutureOrPresent annotation
    @NotNull(message = "Field cannot be null")
    //@FutureOrPresent
    private LocalDateTime checkIn;

    @NotNull(message = "Field cannot be null")
    //@FutureOrPresent
    private LocalDateTime checkOut;

    @NotNull(message = "Field cannot be null")
    @PositiveOrZero
    private Integer discount_percent;

    @NotNull(message = "Field cannot be null")
    @PositiveOrZero
    private Long guestId;

    @NotNull(message = "Field cannot be null")
    @NotEmpty(message = "Field cannot be empty")
    private List<Integer> roomNumbers;
}
