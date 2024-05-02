package com.nhhp.identityservices.service;

import com.nhhp.identityservices.dto.request.UserCreationRequest;
import com.nhhp.identityservices.dto.request.UserUpdateRequest;
import com.nhhp.identityservices.dto.response.UserResponse;
import com.nhhp.identityservices.entity.User;
import com.nhhp.identityservices.enums.Role;
import com.nhhp.identityservices.exception.AppException;
import com.nhhp.identityservices.exception.ErrorCode;
import com.nhhp.identityservices.mapper.UserMapper;
import com.nhhp.identityservices.repository.RoleRepository;
import com.nhhp.identityservices.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){

        log.info("UserService");

        if(userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("User existed");
            throw new AppException(ErrorCode.USER_EXISTED);
        }
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        // same code above
        User user = userMapper.toUser(request);

//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // tao Role
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//--        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

//    public List<User> getUsers(){
//        return userRepository.findAll();
//    }

    @PreAuthorize("hasRole('ADMIN')") // check role truoc khi cho phep truy cap phuong thuc, neu dung se cho truy cap, khong thi dung lai
    public List<UserResponse> getUsers(){
        log.info("success");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }


    @PostAuthorize("hasRole('ADMIN')") // khi method duoc thuc hien xong, no moi check role
    public UserResponse getUser(String id){
        log.info("can access");
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        System.out.println("user: "+user.getId()+" - "+user.getUsername()
                +" - "+user.getPassword()+" - "+user.getLastName()
                +" - "+user.getFirstName()+" - "+user.getRoles());
        System.out.println("role: "+request.getRoles());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        userMapper.updateUser(user, request);
        System.out.println("-----------------1");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        System.out.println("-----------------2");
        var roles = roleRepository.findAllById(request.getRoles());
        System.out.println("-----------------3");
        user.setRoles(new HashSet<>(roles));
        System.out.println("-----------------4");
        return userMapper.toUserResponse(userRepository.save(user));
    }

    // normally, we need parameter to get info, but in this case, we don't need
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
