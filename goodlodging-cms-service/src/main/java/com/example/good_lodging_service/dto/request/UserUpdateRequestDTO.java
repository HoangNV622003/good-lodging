package com.example.good_lodging_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequestDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    String gender;
    String urlAvatar;
    LocalDate birthday;
    Integer status;
}
