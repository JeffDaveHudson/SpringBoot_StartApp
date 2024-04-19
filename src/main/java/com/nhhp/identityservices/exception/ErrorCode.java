package com.nhhp.identityservices.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

// định nghĩa bộ mã code và message ứng với exception tương ứng để hiển thị ra json
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    USER_EXISTED(1003, "User existed", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(1006, "User not existed", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1005, "Username must be at least 4 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 4 characters", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR)
    ;



    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
