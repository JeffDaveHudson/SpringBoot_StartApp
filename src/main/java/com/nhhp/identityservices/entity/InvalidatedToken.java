package com.nhhp.identityservices.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * Lớp này dùng để lưu trữ token được logout
 * Bảng này sẽ chứa những token với expireTime, và sẽ xây dựng tool để quét những token hết expiryTime và xóa token đó,
 * đảm bảo bảng dữ liệu không bị phình to
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime;
}
