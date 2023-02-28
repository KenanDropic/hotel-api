package com.hotel.controller;

import com.hotel.entity.Guest;
import com.hotel.service.GuestService;
import com.hotel.utils.dto.Guest.CreateGuestDto;
import com.hotel.utils.dto.Guest.SearchGuestDto;
import com.hotel.utils.dto.Guest.UpdateGuestDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import com.hotel.utils.projections.GuestReservationsView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getGuests
            (@Valid SearchGuestDto searchParams,
             @RequestParam(required = false, defaultValue = "1") Integer page,
             @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return this.guestService.findAllGuestsWithPaginationAndSorting(searchParams, page, pageSize);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<Guest>> getGuest(@PathVariable("id") final Long guestId) {
        return this.guestService.findGuest(guestId);
    }

    @GetMapping(path = "/{id}/reservations")
    public ResponseEntity<ResponsePayload<Object>> getGuestReservations
            (@PathVariable("id") final Long guestId) {
        return this.guestService.findGuestReservations(guestId);
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<Guest>> createGuest(@RequestBody @Valid CreateGuestDto params) {
        return this.guestService.createGuest(params);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<Guest>> updateGuest(@PathVariable("id") final Long guestId,
                                                              @RequestBody @Valid UpdateGuestDto params) {
        return this.guestService.updateGuest(guestId, params);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<ResponseBody>> deleteGuest(@PathVariable("id") final Long guestId) {
        return this.guestService.deleteGuest(guestId);
    }
}
