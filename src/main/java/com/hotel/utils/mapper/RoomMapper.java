package com.hotel.utils.mapper;

import com.hotel.entity.Room;
import com.hotel.utils.dto.Room.UpdateRoomDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "currentPrice", source = "dto.currentPrice")
    void updateEntityFromDto(UpdateRoomDto dto, @MappingTarget Room room);
}
