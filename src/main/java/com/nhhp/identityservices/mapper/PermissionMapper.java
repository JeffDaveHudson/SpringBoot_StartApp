package com.nhhp.identityservices.mapper;

import com.nhhp.identityservices.dto.request.PermissionRequest;
import com.nhhp.identityservices.dto.request.UserCreationRequest;
import com.nhhp.identityservices.dto.request.UserUpdateRequest;
import com.nhhp.identityservices.dto.response.PermissionResponse;
import com.nhhp.identityservices.dto.response.UserResponse;
import com.nhhp.identityservices.entity.Permission;
import com.nhhp.identityservices.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
