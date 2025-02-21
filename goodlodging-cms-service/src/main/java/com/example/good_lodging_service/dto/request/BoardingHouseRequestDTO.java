package com.example.good_lodging_service.dto.request;

import com.example.good_lodging_service.dto.response.RoomResponseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardingHouseRequestDTO {
    Long userId;
    String name;
    String address;
    String phone;
    String email;
    Float electricityPrice;
    Float waterPrice;
    Boolean hasElevator;
}
