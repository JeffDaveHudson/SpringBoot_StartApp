package com.nhhp.identityservices.mapper;

import com.nhhp.identityservices.dto.request.UserCreationRequest;
import com.nhhp.identityservices.dto.request.UserUpdateRequest;
import com.nhhp.identityservices.dto.response.UserResponse;
import com.nhhp.identityservices.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    // User chứa List<Role> roles
    // UserUpdateRequest chứa List<String> roles
    // => cần ignore thuộc tính role để không map 2 đứa nó
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
