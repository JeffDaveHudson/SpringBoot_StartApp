package com.nhhp.identityservices.dto.request;

import com.nhhp.identityservices.exception.ErrorCode;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //giúp tạo đối tượng và set value cho thuộc tính ngắn gọn hơn, thay vì phải new object, rồi setAttribute từng cái
@FieldDefaults(level = AccessLevel.PRIVATE) //set access modifier mặc định cho all trường
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    @Size(min = 3, message = "PASSWORD_INVALID")
    String password;
    String lastName;
    String firstName;
    LocalDate dob;
    Set<String> roles;
}
