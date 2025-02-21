package com.example.good_lodging_service.mapper;

import com.example.good_lodging_service.dto.request.BoardingHouseRequestDTO;
import com.example.good_lodging_service.dto.response.BoardingHouseResponseDTO;
import com.example.good_lodging_service.entity.BoardingHouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardingHouseMapper {
    BoardingHouse toBoardingHouse(BoardingHouseRequestDTO requestDTO);
    BoardingHouseResponseDTO toBoardingHouseResponseDTO(BoardingHouse boardingHouse);
}
