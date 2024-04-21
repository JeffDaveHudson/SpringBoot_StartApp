package com.nhhp.identityservices.controller;

import com.nhhp.identityservices.dto.request.ApiResponse;
import com.nhhp.identityservices.dto.request.UserCreationRequest;
import com.nhhp.identityservices.dto.request.UserUpdateRequest;
import com.nhhp.identityservices.dto.response.UserResponse;
import com.nhhp.identityservices.entity.User;
import com.nhhp.identityservices.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserController {

//    @Autowired
    UserService userService;

    @PostMapping
//  @RequestBody: map data của request vào object
//  đây là dạng chưa chuẩn hóa api
//  public User createUser(@RequestBody @Valid UserCreationRequest request){
//        return userService.createUser(request);
//}

    // đây là dạng đã chuẩn hóa api
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

//    @GetMapping
//    public List<User> getUsers(){
//        return userService.getUsers();
//    }
    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username: "+authentication.getName());
        authentication.getAuthorities().forEach(
                grantedAuthority -> log.info(grantedAuthority.getAuthority())
        );
        log.info("scope: "+ authentication.getAuthorities());

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

//  @PathVariable: map info of value in {} into variable userId
//  or map by @PathVariable("userId" /* <== */) String userId
    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable("userId") String userId,
                           @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        return "Deleted";
    }
}
