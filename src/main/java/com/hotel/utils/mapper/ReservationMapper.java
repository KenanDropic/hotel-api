package com.hotel.utils.mapper;

import com.hotel.entity.Reservation;
import com.hotel.utils.dto.Reservation.UpdateReservationDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "checkIn",source = "dto.checkIn")
    @Mapping(target = "checkOut",source = "dto.checkOut")
    void updateEntityFromDto(UpdateReservationDto dto, @MappingTarget Reservation reservation);
}
