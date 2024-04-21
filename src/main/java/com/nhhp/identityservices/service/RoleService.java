package com.nhhp.identityservices.service;

import com.nhhp.identityservices.dto.request.ApiResponse;
import com.nhhp.identityservices.dto.request.RoleRequest;
import com.nhhp.identityservices.dto.response.RoleResponse;
import com.nhhp.identityservices.entity.Role;
import com.nhhp.identityservices.mapper.RoleMapper;
import com.nhhp.identityservices.repository.PermissionRepository;
import com.nhhp.identityservices.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {

    RoleMapper roleMapper;

    RoleRepository roleRepository;

    PermissionRepository permissionRepository;

    // permission chỉ cần name và description, nhưng role cần thêm list các permission, vì 1 role có thể có nhiều permission
    // còn permission thì không cần có gì cả
    public RoleResponse createRole(RoleRequest request){
        var role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());
        // permission la List, ta can convert sang Set
        role.setPermissions(new HashSet<>(permission));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var role = roleRepository.findAll();
        return role.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
