package com.nhhp.identityservices.controller;

import com.nhhp.identityservices.dto.request.ApiResponse;
import com.nhhp.identityservices.dto.request.PermissionRequest;
import com.nhhp.identityservices.dto.request.RoleRequest;
import com.nhhp.identityservices.dto.response.PermissionResponse;
import com.nhhp.identityservices.dto.response.RoleResponse;
import com.nhhp.identityservices.service.PermissionService;
import com.nhhp.identityservices.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> deleteRole(@PathVariable String role){
        roleService.delete(role);
        return ApiResponse.<Void>builder()
                .build();
    }


}
