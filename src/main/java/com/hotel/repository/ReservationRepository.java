package com.hotel.repository;

import com.hotel.entity.Reservation;
import com.hotel.utils.dto.Reservation.SearchReservationDto;
import com.hotel.utils.projections.ReservationsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = """
            SELECT rr.reservation_id,re.discount_percent,re.check_in as checkIn,
            re.check_out as checkOut,
            r.allowed_pet,r.description,r.room_number,r.room_type,
            i.invoice_amount,i.invoice_date,
            g.date_of_birth as guest_date_of_birth,concat(g.first_name,' ',g.last_name) as guest_name,g.email as guest_email
            FROM room_reservation rr
            INNER JOIN reservation re on re.reservation_id = rr.reservation_id
            INNER JOIN room r on r.room_id = rr.room_id
            INNER JOIN guest g on g.guest_id = re.guest_id
            INNER JOIN (SELECT invoice_amount,invoice_date,invoice_id FROM invoice
            WHERE :#{#params.invoiceAmount} is null or (
            CASE
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'greater'
            THEN cast(invoice_amount as numeric) >= cast(cast(:#{#params.invoiceAmount} as character varying) as numeric)
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'less'
            THEN cast(invoice_amount as numeric) <= cast(cast(:#{#params.invoiceAmount} as character varying) as numeric)
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'equals'
            THEN cast(invoice_amount as numeric) = cast(cast(:#{#params.invoiceAmount} as character varying) as numeric) END)
            ) i ON i.invoice_id = re.invoice_id
            WHERE :#{#params.checkInFromDate} is null or
            cast(re.check_in as character varying) BETWEEN SYMMETRIC
            cast(:#{#params.checkInFromDate} as character varying) AND cast(:#{#params.checkInToDate} as character varying)
            AND :#{#params.checkOutFromDate} is null or
            cast(re.check_out as character varying) BETWEEN SYMMETRIC
            cast(:#{#params.checkOutFromDate} as character varying) AND cast(:#{#params.checkOutToDate} as character varying)""",
            nativeQuery = true)
    Page<ReservationsView> findAllReservations(SearchReservationDto params, Pageable pageable);

    // ?checkInFromDate=2022-12-05&checkInToDate=2022-12-31&direction=DESC

    @Query(value = """
            SELECT rr.reservation_id,discount_percent,re.check_in as checkIn,re.check_out as checkOut,
            r.allowed_pet,r.description,r.room_number,r.room_type,
            i.invoice_amount,i.invoice_date,
            g.date_of_birth as guest_date_of_birth,concat(g.first_name,' ',g.last_name) as guest_name,g.email as guest_email
            FROM room_reservation rr
            INNER JOIN reservation re on re.reservation_id = rr.reservation_id
            INNER JOIN room r on r.room_id = rr.room_id
            INNER JOIN guest g on g.guest_id = re.guest_id
            LEFT JOIN invoice i on i.invoice_id = re.invoice_id
            WHERE re.reservation_id = :reservationId
            """, nativeQuery = true)
    ReservationsView findOneReservation(@Param("reservationId") Long reservationId);

    @Modifying
    @Query(value = """
            INSERT INTO room_reservation (room_id, reservation_id)
            VALUES (
            cast(cast(:#{#roomId} as text) as bigint),
            cast(cast(:#{#reservationId} as text) as bigint)
            );""", nativeQuery = true)
    void insertRoomReservation(Long roomId, Long reservationId);

    @Modifying
    @Query(value = """
            DELETE FROM room_reservation WHERE reservation_id = :reservationId
            """, nativeQuery = true)
    void deleteRoomReservation(@Param("reservationId") Long reservationId);

}
