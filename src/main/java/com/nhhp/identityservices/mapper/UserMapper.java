package com.nhhp.identityservices.mapper;

import com.nhhp.identityservices.dto.request.UserCreationRequest;
import com.nhhp.identityservices.dto.request.UserUpdateRequest;
import com.nhhp.identityservices.dto.response.UserResponse;
import com.nhhp.identityservices.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
