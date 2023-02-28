package com.hotel.repository;

import com.hotel.entity.Guest;
import com.hotel.utils.dto.Guest.SearchGuestDto;
import com.hotel.utils.projections.GuestReservationsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    @Query(value = """
            SELECT g.first_name as firstName,g.last_name as lastName,g.email,g.phone,
            g.guest_id,g.date_of_birth as DateOfBirth,g.gender,g.created_at,g.updated_at,
            jsonb_agg(r.*) as reservations FROM guest g
            INNER JOIN reservation r on g.guest_id = r.guest_id
            WHERE g.guest_id = :guestId
            GROUP BY g.guest_id
            """, nativeQuery = true)
    GuestReservationsView findGuestReservations(@Param(value = "guestId") Long guestId);

    @Query(value = """
            SELECT * FROM guest g
            WHERE :#{#params.firstName} is null or
            g.first_name = cast(:#{#params.firstName} as character varying)
            AND
            :#{#params.lastName} is null or
            g.last_name = cast(:#{#params.lastName} as character varying)
            """, nativeQuery = true)
    Page<Guest> findGuests(SearchGuestDto params, Pageable pageable);

}
