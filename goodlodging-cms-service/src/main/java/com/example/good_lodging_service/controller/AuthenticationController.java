package com.example.good_lodging_service.controller;

import com.example.good_lodging_service.dto.ApiResponse;
import com.example.good_lodging_service.dto.request.AuthenticationRequestDTO;
import com.example.good_lodging_service.dto.response.AuthenticationResponseDTO;
import com.example.good_lodging_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/token")
    public ApiResponse<AuthenticationResponseDTO> token(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        return ApiResponse.<AuthenticationResponseDTO>builder().result(authenticationService.authenticate(authenticationRequestDTO)).build();
    }
}
