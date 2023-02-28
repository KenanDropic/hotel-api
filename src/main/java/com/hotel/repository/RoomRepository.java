package com.hotel.repository;

import com.hotel.entity.Room;
import com.hotel.utils.dto.Room.IsRoomAvailableDto;
import com.hotel.utils.dto.Room.SearchRoomDto;
import com.hotel.utils.projections.RoomReservationsView;
import com.hotel.utils.projections.ReservationsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(value = """
            SELECT * FROM room r
            WHERE (:#{#params.allowedPet} is null or
            r.allowed_pet = cast(cast(:#{#params.allowedPet} as text) as boolean))
            AND (:#{#params.type} is null or
            r.room_type = cast(:#{#params.type} as character varying))
            AND (:#{#params.price} is null or (
            CASE
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'greater'
            THEN cast(current_price as numeric) >= cast(cast(:#{#params.price} as text) as numeric)
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'less'
            THEN cast(current_price as numeric) <= cast(cast(:#{#params.price} as text) as numeric)
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'equals'
            THEN cast(current_price as numeric) = cast(cast(:#{#params.price} as text) as numeric) END))
            """, nativeQuery = true)
    Page<Room> findAllRooms(SearchRoomDto params, Pageable pageable);

    @Query(value = """
            SELECT rr.reservation_id,re.check_in as check_in,re.check_out as check_out,
            r.allowed_pet,r.room_number,r.room_type,
            g.date_of_birth as guest_date_of_birth,concat(g.first_name,' ',g.last_name) as guest_name,g.email as guest_email
            FROM room_reservation rr
            INNER JOIN reservation re on re.reservation_id = rr.reservation_id
            INNER JOIN LATERAL (
            SELECT room_id,allowed_pet,description,room_number,room_type
                FROM room\s
                WHERE room_id = :roomId
                AND re.check_out >= cast(:#{#params.checkInDate} as timestamp without time zone)\s
                AND re.check_in <= cast(:#{#params.checkOutDate} as timestamp without time zone)
            ) r ON r.room_id = rr.room_id
            INNER JOIN guest g on g.guest_id = re.guest_id
            """, nativeQuery = true)
    ReservationsView findOutIfRoomIsAvailable(@Param("roomId") Long roomId, IsRoomAvailableDto params);

    @Query(value = """
            SELECT json_build_object(
                   'reservationId',re.reservation_id,
                   'checkIn',check_in,
                   'checkOut',check_out,
                   'discount',discount_percent,
                   'invoiceId',i.invoice_id,
                   'invoiceAmount',i.invoice_amount,
                   'invoiceDate',i.invoice_date,
                   'guestId',g.guest_id,
                   'guestName',concat(g.first_name,' ',g.last_name),
                   'guestEmail',g.email,
                   'guestPhone',g.phone
                   )
            FROM room r
            INNER JOIN room_reservation rr ON r.room_id = rr.room_id
            INNER JOIN reservation re ON re.reservation_id = rr.reservation_id
            INNER JOIN guest g on g.guest_id = re.guest_id
            INNER JOIN invoice i ON i.invoice_id = re.invoice_id
            WHERE r.room_id = :roomId""", nativeQuery = true)
    Page<?> findReservationsForRoom(@Param("roomId") Long roomId,Pageable pageable);

   Optional<Room> findByRoomNumber(Integer roomNumber);
}
