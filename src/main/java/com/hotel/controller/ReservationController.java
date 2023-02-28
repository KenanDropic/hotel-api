package com.hotel.controller;

import com.hotel.entity.Reservation;
import com.hotel.service.ReservationService;
import com.hotel.utils.dto.Reservation.CreateReservationDto;
import com.hotel.utils.dto.Reservation.SearchReservationDto;
import com.hotel.utils.dto.Reservation.UpdateReservationDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import com.hotel.utils.projections.ReservationsView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getReservations
            (@Valid SearchReservationDto searchParams,
             @RequestParam(required = false, defaultValue = "1") Integer page,
             @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return this.reservationService.findAllReservationsWithPaginationAndSorting(searchParams, page, pageSize);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<ReservationsView>> getReservations(@PathVariable("id") final Long id) {
        return this.reservationService.findReservation(id);
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<Reservation>> createReservation
            (@RequestBody @Valid CreateReservationDto params) {
        return this.reservationService.createReservation(params);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<Reservation>> updateReservation
            (@PathVariable("id") final Long id, @RequestBody @Valid UpdateReservationDto dto) {
        return this.reservationService.updateReservation(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<ResponseBody>> deleteReservation
            (@PathVariable("id") final Long id){
        return this.reservationService.deleteReservation(id);
    }
}
