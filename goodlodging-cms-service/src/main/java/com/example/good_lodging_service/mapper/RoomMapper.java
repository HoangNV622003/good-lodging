package com.example.good_lodging_service.mapper;

import com.example.good_lodging_service.dto.request.RoomRequestDTO;
import com.example.good_lodging_service.dto.response.RoomResponseDTO;
import com.example.good_lodging_service.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toRoom(RoomRequestDTO requestDTO);
    RoomResponseDTO toRoomResponseDTO(Room room);
}
