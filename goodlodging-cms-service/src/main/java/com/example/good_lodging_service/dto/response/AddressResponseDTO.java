package com.example.good_lodging_service.dto.response;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponseDTO {
    Long id;
    Integer houseNumber;
    String streetName;
    Long wardsId;
    Long districtId;
    Long provinceId;
    Integer status;
}
