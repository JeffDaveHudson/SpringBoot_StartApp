package com.nhhp.identityservices.mapper;

import com.nhhp.identityservices.dto.request.PermissionRequest;
import com.nhhp.identityservices.dto.request.RoleRequest;
import com.nhhp.identityservices.dto.response.PermissionResponse;
import com.nhhp.identityservices.dto.response.RoleResponse;
import com.nhhp.identityservices.entity.Permission;
import com.nhhp.identityservices.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // vì RoleRequest dùng Set<String>, mà Role trong entity dùng Set<Permission>,
    // ta cần ignore nó (khi map sẽ bỏ qua attribute permission), thao tác thêm như sau
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
