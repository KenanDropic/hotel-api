package com.hotel.controller;

import com.hotel.entity.Room;
import com.hotel.service.RoomService;
import com.hotel.utils.dto.Room.CreateRoomDto;
import com.hotel.utils.dto.Room.RoomReservationDto;
import com.hotel.utils.dto.Room.SearchRoomDto;
import com.hotel.utils.dto.Room.UpdateRoomDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getRooms
            (@Valid SearchRoomDto searchParams,
             @RequestParam(required = false, defaultValue = "1") Integer page,
             @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return this.roomService.findAllRoomsWithPaginationAndSorting(searchParams, page, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload<Room>> getRoom
            (@PathVariable("id") final Long roomId) {
        return this.roomService.findRoom(roomId);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<ResponsePayload<HashMap<String, Object>>> getRoomReservations
            (@PathVariable("id") final Long roomId,
             @Valid RoomReservationDto params,
             @RequestParam(required = false, defaultValue = "1") Integer page,
             @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return this.roomService.findRoomReservations(roomId, params, page, pageSize);
    }


    @PostMapping
    public ResponseEntity<ResponsePayload<Room>> createRoom
            (@RequestBody @Valid CreateRoomDto params) {
        return this.roomService.createRoom(params);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload<Room>> updateRoom
            (@PathVariable("id") final Long roomId,
             @RequestBody @Valid UpdateRoomDto updateParams) {
        return this.roomService.updateRoom(roomId, updateParams);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload<ResponseBody>> deleteRoom
            (@PathVariable("id") final Long roomId) {
        return this.roomService.deleteRoom(roomId);
    }
}
