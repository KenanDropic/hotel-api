package com.hotel.service;

import com.hotel.entity.*;
import com.hotel.exception.exceptions.BadRequestExc;
import com.hotel.exception.exceptions.NotFoundExc;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.utils.UpdateBodyValidation;
import com.hotel.utils.dto.Reservation.UpdateReservationDto;
import com.hotel.utils.dto.Room.IsRoomAvailableDto;
import com.hotel.utils.mapper.ReservationMapper;
import com.hotel.utils.paginationSorting.Pagination;
import com.hotel.utils.paginationSorting.Sorting;
import com.hotel.utils.dto.Reservation.CreateReservationDto;
import com.hotel.utils.dto.Reservation.SearchReservationDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import com.hotel.utils.projections.ReservationsView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReservationMapper reservationMapper;


    public ReservationService(ReservationRepository reservationRepository,
                              GuestRepository guestRepository,
                              RoomRepository roomRepository,
                              ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationMapper = reservationMapper;
    }

    public ResponseEntity<PaginationResponse> findAllReservationsWithPaginationAndSorting(
            SearchReservationDto params, Integer page, Integer pageSize
    ) {
        Sorting sorting = new Sorting();
        sorting.containsDirection(params.getDirection());
        sorting.containsField(Arrays.asList("check_in", "check_out", "created_at","reservation_id"), params.getField());

        Sort sort = params.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(params.getField()).equals("created_at") ?
                        "re.created_at" :
                        Objects.requireNonNull(params.getField()).equals("reservation_id") ?
                                "reservation_id" :
                                Objects.requireNonNull(params.getField()).equals("check_in") ?
                                        "re.start_date" :
                                        "re.end_date").ascending() :
                Sort.by(Objects.requireNonNull(params.getField()).equals("created_at") ?
                        "re.created_at" :
                        Objects.requireNonNull(params.getField()).equals("reservation_id") ?
                                "reservation_id" :
                                Objects.requireNonNull(params.getField()).equals("check_in") ?
                                        "re.start_date" :
                                        "re.end_date").descending();

        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Page<ReservationsView> reservations = this.reservationRepository.findAllReservations(params, paging);

        if (reservations.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0, reservations.getTotalPages(),
                            page, reservations.getContent()));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(reservations, page);


        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, reservations.getSize(),
                        reservations.getTotalElements(), reservations.getTotalPages(), page,
                        pagination.getPagination(), reservations.getContent()));
    }

    public ResponseEntity<ResponsePayload<ReservationsView>> findReservation(Long id) {
        ReservationsView reservation = this.reservationRepository.findOneReservation(id);

        if (reservation == null) {
            throw new NotFoundExc("Reservation " + id + " not found!");
        }

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, reservation));
    }

    public ResponseEntity<ResponsePayload<Reservation>> createReservation(CreateReservationDto params) {
        Guest guest = this.guestRepository
                .findById(params.getGuestId())
                .orElseThrow(() -> new NotFoundExc("Guest " + params.getGuestId() + " not found!"));


        List<Room> rooms = params
                .getRoomNumbers()
                .stream()
                .map((id) -> this.roomRepository
                        .findByRoomNumber(id)
                        .orElseThrow(() -> new NotFoundExc("Room " + id + " not found!"))
                )
                .toList();

        List<ReservationsView> areRoomsFree = rooms
                .stream()
                .map((room ->
                        this.roomRepository.findOutIfRoomIsAvailable(room.getId(),
                                new IsRoomAvailableDto(
                                        params.getCheckIn().toString(),
                                        params.getCheckOut().toString()))
                ))
                .filter(Objects::nonNull)
                .toList();

        if (areRoomsFree.size() > 0) {
            if (areRoomsFree.size() == 1) {
                throw new BadRequestExc("We cannot book this reservation.Room number " +
                        areRoomsFree.get(0).getRoom_Number() + " is already reserved for that period.");
            }
            List<Integer> roomNumbers = areRoomsFree.stream().map((ReservationsView::getRoom_Number)).toList();
            throw new BadRequestExc("We cannot book this reservation. Room numbers "
                    + roomNumbers + " are already reserved for that period.");

        }

        float days = Precision.round(((float) ChronoUnit.HALF_DAYS.between(params.getCheckIn(), params.getCheckOut()) / 2), 1);
        float diff = days - ChronoUnit.DAYS.between(params.getCheckIn(), params.getCheckOut());
        if (diff >= 0.5) {
            days += 0.5;
        }
        final float finalDays = days;

        double priceWithoutDiscount = rooms
                .stream()
                .map(Room::getCurrentPrice)
                .reduce((double) 0, Double::sum);
        double discount = ((double) params.getDiscount_percent() / 100) * priceWithoutDiscount;
        double price = (priceWithoutDiscount - discount) * finalDays;

        Invoice invoice = new Invoice(price, LocalDateTime.now());
        Reservation reservation = new Reservation(params.getCheckIn(),
                params.getCheckOut(), params.getDiscount_percent(),
                guest, rooms, invoice);

        this.reservationRepository.save(reservation);

        rooms.forEach((room -> this
                .reservationRepository
                .insertRoomReservation(room.getId(), reservation.getId())));

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Reservation created successfully",
                        reservation));
    }

    public ResponseEntity<ResponsePayload<Reservation>> updateReservation(Long id, UpdateReservationDto dto) {
        Reservation reservation = this.reservationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundExc("Reservation " + id + " not found."));

        List<LocalDateTime> existingValues = Arrays.asList(reservation.getCheckIn(), reservation.getCheckOut());
        List<Object> passedValues = Arrays.asList(dto.getCheckIn(), dto.getCheckOut());

        UpdateBodyValidation<LocalDateTime> check = new UpdateBodyValidation<>();
        check.checkRequestBody(existingValues, passedValues);

        if ((dto.getCheckIn() != null && dto.getCheckIn().isAfter(reservation.getCheckOut())) ||
                (dto.getCheckOut() != null && dto.getCheckOut().isBefore(reservation.getCheckIn()))) {
            throw new BadRequestExc("Check in date cannot be after check out date. " +
                    "Check out date cannot be before check in date.");
        }


        reservationMapper.updateEntityFromDto(dto, reservation);
        this.reservationRepository.save(reservation);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Reservation " + id + " updated successfully",
                        reservation
                ));
    }

    public ResponseEntity<ResponsePayload<ResponseBody>> deleteReservation(Long reservationId) {
        Reservation reservation = this.reservationRepository
                .findById(reservationId)
                .orElseThrow(() -> new NotFoundExc("Reservation " + reservationId + " not found!"));

        this.reservationRepository.deleteRoomReservation(reservationId);
        this.reservationRepository.delete(reservation);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Reservation " + reservationId + " deleted successfully"));
    }
}
