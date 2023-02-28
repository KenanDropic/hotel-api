package com.hotel.utils.dto.Reservation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;



@Data
@NoArgsConstructor
public class SearchReservationDto {
    @Nullable
    private String checkInFromDate; // when searching first is year,then month,then days. yyyy/mm/dd
    @Nullable
    private String checkInToDate;

    @Nullable
    private String checkOutFromDate;
    @Nullable
    private String checkOutToDate;

    @Nullable
    private String invoiceAmount;

    private String comparisonOperator = "greater";
    private String field = "created_at";
    private String direction = "ASC";
}
