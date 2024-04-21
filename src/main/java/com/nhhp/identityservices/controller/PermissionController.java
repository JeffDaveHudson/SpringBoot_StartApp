package com.nhhp.identityservices.controller;

import com.nhhp.identityservices.dto.request.ApiResponse;
import com.nhhp.identityservices.dto.request.PermissionRequest;
import com.nhhp.identityservices.dto.response.PermissionResponse;
import com.nhhp.identityservices.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermission(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermission())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> deletePermission(@PathVariable String permission){
        permissionService.deletePermission(permission);
        return ApiResponse.<Void>builder()
                .build();
    }


}
