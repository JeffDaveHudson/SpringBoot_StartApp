package com.nhhp.identityservices.dto.request;

import com.nhhp.identityservices.validator.DobConstraint;
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

    @DobConstraint(min = 2, message = "INVALID_DATE_OF_BIRTH")
    LocalDate dob;
    // khi truyền vào chỉ cần list String
    List<String> roles;
}
