package com.example.good_lodging_service.mapper;

import com.example.good_lodging_service.dto.request.DeviceRequestDTO;
import com.example.good_lodging_service.dto.response.DeviceResponseDTO;
import com.example.good_lodging_service.entity.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    Device toDevice(DeviceRequestDTO requestDTO);
    DeviceResponseDTO toDeviceResponseDTO(Device device);
}
