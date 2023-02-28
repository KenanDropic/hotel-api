package com.hotel.utils.mapper;

import com.hotel.entity.Guest;
import com.hotel.utils.dto.Guest.UpdateGuestDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateEntityFromDto(UpdateGuestDto dto, @MappingTarget Guest guest);
}
