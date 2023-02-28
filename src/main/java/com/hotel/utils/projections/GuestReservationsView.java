package com.hotel.utils.projections;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotel.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GuestReservationsView {
    Long getGuest_Id();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPhone();

    String getGender();

    LocalDate getDateOfBirth();

    LocalDateTime getCreated_At();

    LocalDateTime getUpdated_At();

    JsonNode getReservations();
}
