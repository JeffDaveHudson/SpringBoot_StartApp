package com.nhhp.identityservices.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;

    // permission chỉ cần name và description, nhưng role cần thêm list các permission, vì 1 role có thể có nhiều permission
    // tác giả chỉ cần name của permission, nền ta dùng String
    Set<String> permissions;

}
