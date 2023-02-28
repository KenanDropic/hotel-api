package com.hotel.service;

import com.hotel.entity.Guest;
import com.hotel.exception.exceptions.NotFoundExc;
import com.hotel.repository.GuestRepository;
import com.hotel.utils.UpdateBodyValidation;
import com.hotel.utils.dto.Guest.UpdateGuestDto;
import com.hotel.utils.mapper.GuestMapper;
import com.hotel.utils.paginationSorting.Pagination;
import com.hotel.utils.paginationSorting.Sorting;
import com.hotel.utils.dto.Guest.CreateGuestDto;
import com.hotel.utils.dto.Guest.SearchGuestDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import com.hotel.utils.projections.GuestReservationsView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestService(GuestRepository guestRepository, GuestMapper guestMapper) {
        this.guestRepository = guestRepository;
        this.guestMapper = guestMapper;
    }

    public ResponseEntity<PaginationResponse> findAllGuestsWithPaginationAndSorting
            (SearchGuestDto searchParams,
             int page, int pageSize) {
        Sorting sorting = new Sorting();
        sorting.containsField(Arrays.asList("guest_id", "first_name", "created_at"), searchParams.getField());
        sorting.containsDirection(searchParams.getDirection());

        Sort sort = searchParams.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("guest_id") ?
                        "guest_id" : Objects.requireNonNull(searchParams.getField()).equals("first_name") ?
                        "first_name" :
                        "created_at").ascending() :
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("guest_id") ?
                        "guest_id" : Objects.requireNonNull(searchParams.getField()).equals("first_name") ?
                        "first_name" :
                        "created_at").descending();

        // subtracting one from page,since in pagination it starts from 0,but in frontend we will send values from 1
        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Page<Guest> guests = this.guestRepository.findGuests(searchParams, paging);

        if (guests.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0,
                            guests.getTotalPages(), page, guests.getContent()));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(guests, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, guests.getSize(),
                        guests.getTotalElements(), guests.getTotalPages(), page,
                        pagination.getPagination(), guests.getContent()));
    }

    public ResponseEntity<ResponsePayload<Guest>> findGuest(Long id) {

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        this.guestRepository
                                .findById(id)
                                .orElseThrow(() -> new NotFoundExc("Guest " + id + " not found!"))));
    }

    public ResponseEntity<ResponsePayload<Object>> findGuestReservations(Long guestId) {
        Guest guest = this.guestRepository
                .findById(guestId)
                .orElseThrow(() -> new NotFoundExc("Guest not found with the given ID!"));

        GuestReservationsView guestReservations = this.guestRepository.findGuestReservations(guest.getId());

        // check if reservations are empty
        System.out.println("Guest reservations: " + guestReservations);
        if(guestReservations == null){
            return ResponseEntity
                    .status(200)
                    .body(new ResponsePayload<>(true,guest));
        }

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, guestReservations));
    }

    public ResponseEntity<ResponsePayload<Guest>> createGuest(CreateGuestDto params) {
        Guest guest = new Guest(params.getFirstName(),
                params.getLastName(),
                params.getEmail(),
                params.getPhone(),
                params.getGender(),
                params.getDateOfBirth());

        return ResponseEntity
                .status(201)
                .body(new ResponsePayload<>(
                        true,
                        "Guest created successfully",
                        this.guestRepository.save(guest)));
    }

    public ResponseEntity<ResponsePayload<Guest>> updateGuest(Long guestId, UpdateGuestDto params) {
        Guest guest = this.guestRepository
                .findById(guestId)
                .orElseThrow(() -> new NotFoundExc("Guest " + guestId + " not found!"));

        List<Object> existingValues = Arrays.asList(guest.getFirstName(), guest.getLastName(),
                guest.getPhone(), guest.getDateOfBirth());
        List<Object> passedValues = Arrays.asList(params.getFirstName(), params.getLastName(),
                params.getPhone(), params.getDateOfBirth());
        UpdateBodyValidation<Object> check = new UpdateBodyValidation<>();
        check.checkRequestBody(existingValues, passedValues);

        guestMapper.updateEntityFromDto(params, guest);
        this.guestRepository.save(guest);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Guest updated successfully",
                        guest));
    }

    public ResponseEntity<ResponsePayload<ResponseBody>> deleteGuest(Long id) {
        Guest guest = this.guestRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundExc("Guest " + id + " not found!"));

        this.guestRepository.deleteById(guest.getId());

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, "Guest " + id + " deleted successfully"));
    }
}
