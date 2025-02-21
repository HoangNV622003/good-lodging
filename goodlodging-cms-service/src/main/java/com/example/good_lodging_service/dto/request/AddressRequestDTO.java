package com.example.good_lodging_service.dto.request;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequestDTO {
    Integer houseNumber;
    String street;
    Long wardsId;
    Long districtId;
    Long provinceId;
}
