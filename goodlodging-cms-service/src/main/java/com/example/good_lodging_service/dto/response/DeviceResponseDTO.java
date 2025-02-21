package com.example.good_lodging_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceResponseDTO {
    Long id;
    Long roomId;
    String name;
    String description;
    String deviceStatus;
    Integer status;
}
