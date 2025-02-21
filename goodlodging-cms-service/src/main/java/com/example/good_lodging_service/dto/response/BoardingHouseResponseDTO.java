package com.example.good_lodging_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardingHouseResponseDTO {
    Long id;
    Long userId;
    String name;
    String address;
    String phone;
    String email;
    Float electricityPrice;
    Float waterPrice;
    Boolean hasElevator;
    List<RoomResponseDTO> rooms;
}
