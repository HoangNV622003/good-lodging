package com.example.good_lodging_service.mapper;

import com.example.good_lodging_service.dto.request.RoleRequestDTO;
import com.example.good_lodging_service.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper{
    Role toRole(RoleRequestDTO requestDTO);
    RoleRequestDTO toRoleRequestDTO(Role role);
}
