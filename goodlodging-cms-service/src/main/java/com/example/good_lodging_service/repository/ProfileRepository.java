package com.example.good_lodging_service.repository;

import com.example.good_lodging_service.constants.ApiResponseCode;
import com.example.good_lodging_service.constants.CommonStatus;
import com.example.good_lodging_service.entity.User;
import com.example.good_lodging_service.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileRepository {
    UserRepository userRepository;
    public ProfileResponseDTO getProfile(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, CommonStatus.ACTIVE.getValue()).orElseThrow(
                ()-> new AppException(ApiResponseCode.ENTITY_NOT_FOUND)
        );

    }
}
