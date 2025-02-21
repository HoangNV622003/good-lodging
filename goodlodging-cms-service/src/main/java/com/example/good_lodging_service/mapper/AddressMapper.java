package com.example.good_lodging_service.mapper;

import com.example.good_lodging_service.dto.request.AddressRequestDTO;
import com.example.good_lodging_service.dto.response.AddressResponseDTO;
import com.example.good_lodging_service.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequestDTO requestDTO);
    AddressResponseDTO toAddressResponseDTO(Address address);
}
