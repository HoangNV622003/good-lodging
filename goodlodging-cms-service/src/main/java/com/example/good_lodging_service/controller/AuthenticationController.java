package com.example.good_lodging_service.controller;

import com.example.good_lodging_service.dto.ApiResponse;
import com.example.good_lodging_service.dto.request.AuthenticationRequestDTO;
import com.example.good_lodging_service.dto.request.IntrospectRequestDTO;
import com.example.good_lodging_service.dto.request.LogoutRequestDTO;
import com.example.good_lodging_service.dto.request.RefreshRequestDTO;
import com.example.good_lodging_service.dto.response.AuthenticationResponseDTO;
import com.example.good_lodging_service.dto.response.IntrospectResponseDTO;
import com.example.good_lodging_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

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

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponseDTO> introspect(@RequestBody IntrospectRequestDTO requestDTO) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponseDTO>builder().result(authenticationService.introspect(requestDTO)).build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequestDTO requestDTO) throws ParseException, JOSEException {
        authenticationService.logout(requestDTO);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponseDTO> refresh(@RequestBody RefreshRequestDTO refreshRequestDTO) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponseDTO>builder().result(authenticationService.refreshToken(refreshRequestDTO)).build();
    }
}
