package com.hotel.utils.projections;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReservationsView {
    Long getReservation_Id();
    Integer getDiscount_Percent();
    LocalDateTime getCheckIn();
    LocalDateTime getCheckOut();
    Boolean getAllowed_Pet();
    String getDescription();
    Integer getRoom_Number();
    String getRoom_Type();
    Double getInvoice_Amount();
    LocalDateTime getInvoice_Date();
    LocalDate getGuest_Date_Of_Birth();
    String getGuest_Name();
    String getGuest_Email();
}
