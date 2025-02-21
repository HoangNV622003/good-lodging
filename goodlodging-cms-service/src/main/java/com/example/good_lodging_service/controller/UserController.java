package com.example.good_lodging_service.controller;

import com.example.good_lodging_service.dto.ApiResponse;
import com.example.good_lodging_service.dto.request.UpdatePasswordRequestDTO;
import com.example.good_lodging_service.dto.request.UserCreateRequestDTO;
import com.example.good_lodging_service.dto.request.UserUpdateRequestDTO;
import com.example.good_lodging_service.dto.response.CommonResponseDTO;
import com.example.good_lodging_service.dto.response.UserResponseDTO;
import com.example.good_lodging_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponseDTO>> getUsers(@PageableDefault(size = 15, sort = "dateUpdated",direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.<List<UserResponseDTO>>builder().result(userService.getAllUsers(pageable)).build();
    }

    @PostMapping
    public ApiResponse<UserResponseDTO> createUser(@RequestBody UserCreateRequestDTO requestDTO) {
        return ApiResponse.<UserResponseDTO>builder().result(userService.createUser(requestDTO)).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponseDTO> getUser(@PathVariable Long userId) {
        return ApiResponse.<UserResponseDTO>builder().result(userService.getUser(userId)).build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponseDTO> updateUser(@PathVariable Long userId,@RequestBody UserUpdateRequestDTO requestDTO) {
        return ApiResponse.<UserResponseDTO>builder().result(userService.updateUser(userId,requestDTO)).build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<CommonResponseDTO> deleteUser(@PathVariable Long userId) {
        return ApiResponse.<CommonResponseDTO>builder().result(userService.deleteUser(userId)).build();
    }

    @PatchMapping("/{userId}")
    public ApiResponse<CommonResponseDTO> updatePassword(@PathVariable Long userId, @RequestBody UpdatePasswordRequestDTO requestDTO) {
        return ApiResponse.<CommonResponseDTO>builder().result(userService.updatePassword(userId,requestDTO)).build();
    }
}
