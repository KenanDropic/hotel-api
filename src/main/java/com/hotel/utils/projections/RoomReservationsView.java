package com.hotel.utils.projections;

import com.fasterxml.jackson.databind.JsonNode;

public interface RoomReservationsView {
    JsonNode getReservations();
}
