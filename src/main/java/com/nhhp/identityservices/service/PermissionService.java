package com.nhhp.identityservices.service;


import com.nhhp.identityservices.dto.request.PermissionRequest;
import com.nhhp.identityservices.dto.response.PermissionResponse;
import com.nhhp.identityservices.entity.Permission;
import com.nhhp.identityservices.mapper.PermissionMapper;
import com.nhhp.identityservices.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);

    }

    public List<PermissionResponse> getAllPermission(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void deletePermission(String permission){
        permissionRepository.deleteById(permission);
    }
}
