package com.hotel.service;

import com.hotel.entity.Room;
import com.hotel.exception.exceptions.NotFoundExc;
import com.hotel.repository.RoomRepository;
import com.hotel.utils.UpdateBodyValidation;
import com.hotel.utils.dto.Room.RoomReservationDto;
import com.hotel.utils.mapper.RoomMapper;
import com.hotel.utils.paginationSorting.Pagination;
import com.hotel.utils.paginationSorting.Sorting;
import com.hotel.utils.dto.Room.CreateRoomDto;
import com.hotel.utils.dto.Room.SearchRoomDto;
import com.hotel.utils.dto.Room.UpdateRoomDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import com.hotel.utils.projections.RoomReservationsView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public ResponseEntity<PaginationResponse> findAllRoomsWithPaginationAndSorting
            (SearchRoomDto searchParams, int page, int pageSize) {
        Sorting sorting = new Sorting();
        sorting.containsDirection(searchParams.getDirection());
        sorting.containsField(List.of("room_number", "current_price", "room_id"), searchParams.getField());

        System.out.println("Search params: " + searchParams);

        Sort sort = searchParams.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("room_number") ?
                        "room_number" :
                        Objects.requireNonNull(searchParams.getField()).equals("current_price") ?
                                "current_price" :
                                "room_id").ascending() :
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("room_number") ?
                        "room_number" :
                        Objects.requireNonNull(searchParams.getField()).equals("current_price") ?
                                "current_price" :
                                "room_id").descending();

        // subtracting one from page,since in pagination it starts from 0,but in frontend we will send values from 1
        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Page<Room> rooms = this.roomRepository.findAllRooms(searchParams, paging);

        if (rooms.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0, rooms.getTotalPages(),
                            page, rooms.getContent()));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(rooms, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, rooms.getSize(),
                        rooms.getTotalElements(), rooms.getTotalPages(), page,
                        pagination.getPagination(), rooms.getContent()));
    }

    public ResponseEntity<ResponsePayload<Room>> findRoom(Long id) {
        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, this.roomRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundExc("Room " + id + " not found"))));
    }

    public ResponseEntity<ResponsePayload<HashMap<String, Object>>> findRoomReservations
            (Long id, RoomReservationDto params, int page, int pageSize) {
        Sorting sorting = new Sorting();
        sorting.containsDirection(params.getDirection());
        sorting.containsField(Arrays.asList(
                "invoiceAmount",
                "startDate",
                "endDate"), params.getField());

        Sort sort = params.getDirection().equals("ASC") ?
                Sort.by(params.getField().equals("invoiceAmount") ? "i.invoice_amount" :
                        params.getField().equals("startDate") ? "re.start_date" :
                                "re.end_date").ascending() :
                Sort.by(params.getField().equals("invoiceAmount") ? "i.invoice_amount" :
                        params.getField().equals("startDate") ? "re.start_date" :
                                "re.end_date").descending();

        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Room room = this.roomRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundExc("Room " + id + " not found!"));
        Page<?> roomReservations = this.roomRepository.findReservationsForRoom(id, paging);

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("room", room);


        if (roomReservations.isEmpty()) {
            payload.put("reservations", "null");
            return ResponseEntity
                    .status(200)
                    .body(new ResponsePayload<>(true, payload));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(roomReservations, page);

        payload.put("reservations", new PaginationResponse(
                true,
                roomReservations.getSize(),
                roomReservations.getTotalElements(),
                roomReservations.getTotalPages(),
                page,
                pagination.getPagination(),
                roomReservations.getContent()));

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, payload));
    }

    public ResponseEntity<ResponsePayload<Room>> createRoom(CreateRoomDto params) {
        if (this.roomRepository.findByRoomNumber(params.getRoom_number()).isPresent()) {
            return ResponseEntity
                    .status(409)
                    .body(new ResponsePayload<>(false, "Room with number " + params.getRoom_number() + " already exists."));
        }

        Room room = new Room(params.getRoom_number(), params.getDescription(), params.getRoom_type(),
                params.getAllowed_pet(), params.getPrice());

        this.roomRepository.save(room);

        return ResponseEntity
                .status(201)
                .body(new ResponsePayload<>(true, room));
    }

    public ResponseEntity<ResponsePayload<Room>> updateRoom(Long id, UpdateRoomDto params) {
        Room room = this.roomRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundExc("Room " + id + " not found"));

        List<Object> existingValues = Arrays.asList(room.getAllowedPet(), room.getCurrentPrice(),
                room.getDescription(), room.getRoomType());
        List<Object> passedValues = Arrays.asList(params.getAllowedPet(), params.getCurrentPrice(),
                params.getDescription(), params.getRoomType());

        UpdateBodyValidation<Object> check = new UpdateBodyValidation<>();
        check.checkRequestBody(existingValues, passedValues);

        roomMapper.updateEntityFromDto(params, room);
        this.roomRepository.save(room);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Room updated successfully!",
                        room));
    }

    public ResponseEntity<ResponsePayload<ResponseBody>> deleteRoom(Long id) {
        Room room = this.roomRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundExc("Room " + id + " not found"));

        this.roomRepository.deleteById(room.getId());

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Room " + id + " deleted successfully"));
    }
}
