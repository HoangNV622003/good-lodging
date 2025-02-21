package com.example.good_lodging_service.service;

import com.example.good_lodging_service.constants.ApiResponseCode;
import com.example.good_lodging_service.constants.CommonStatus;
import com.example.good_lodging_service.dto.request.AddressRequestDTO;
import com.example.good_lodging_service.dto.response.AddressResponseDTO;
import com.example.good_lodging_service.dto.response.CommonResponseDTO;
import com.example.good_lodging_service.entity.Address;
import com.example.good_lodging_service.exception.AppException;
import com.example.good_lodging_service.mapper.AddressMapper;
import com.example.good_lodging_service.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    public AddressResponseDTO createBoardingHouseAddress(AddressRequestDTO addressRequestDTO) {
        if (addressRepository.existsByHouseNumberAndStreetNameAndWardsIdAndDistrictIdAndProvinceIdAndStatus(
                addressRequestDTO.getHouseNumber(),
                addressRequestDTO.getStreet(),
                addressRequestDTO.getWardsId(),
                addressRequestDTO.getDistrictId(),
                addressRequestDTO.getProvinceId(),
                CommonStatus.ACTIVE.getValue()))
            throw new AppException(ApiResponseCode.ADDRESS_ALREADY_EXISTS);
        return addressMapper.toAddressResponseDTO(addressRepository.save(addressMapper.toAddress(addressRequestDTO)));
    }

    public AddressResponseDTO updateBoardingHouseAddress(Long addressId, AddressRequestDTO addressRequestDTO) {
        if (addressRepository.existsByHouseNumberAndStreetNameAndWardsIdAndDistrictIdAndProvinceIdAndStatusAndIdNot(
                addressRequestDTO.getHouseNumber(),
                addressRequestDTO.getStreet(),
                addressRequestDTO.getWardsId(),
                addressRequestDTO.getDistrictId(),
                addressRequestDTO.getProvinceId(),
                CommonStatus.ACTIVE.getValue(),
                addressId))
            throw new AppException(ApiResponseCode.ADDRESS_ALREADY_EXISTS);
        return addressMapper.toAddressResponseDTO(addressRepository.save(addressMapper.toAddress(addressRequestDTO)));
    }

    public CommonResponseDTO deleteBoardingHouseAddress(Long addressId){
        Address address = findById(addressId);
        address.setStatus(CommonStatus.DELETED.getValue());
        addressRepository.save(address);
        return CommonResponseDTO.builder().result(ApiResponseCode.ADDRESS_DELETED_SUCCESSFULLY.getMessage()).build();
    }

    public Address findById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(()-> new AppException(ApiResponseCode.ENTITY_NOT_FOUND));
    }

}
