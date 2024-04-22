package com.nhhp.identityservices.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String lastName;
    String firstName;
    LocalDate dob;
    // khi truyền vào chỉ cần list String
    List<String> roles;
}
