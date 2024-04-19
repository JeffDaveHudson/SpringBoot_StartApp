package com.nhhp.identityservices.dto.request;

/*bình thường api trả về như sau:
        [
        {
        "id": "67f2a74c-04e3-4bb8-af56-19f833732e30",
        "username": "user2",
        "password": "pass2",
        "lastName": "tran",
        "firstName": "anh",
        "dob": "2001-10-02"
        }
        ]
ta cần chuẩn hóa nó dạng như
{
    "code": 1000,
    "message": null,
    "result": {
        "id": "662d77b7-3300-4d43-868e-5849bbf5790d",
        "username": "user7",
        "password": "pass7",
        "lastName": "alice",
        "firstName": "van an",
        "dob": "1988-01-02"
    }
}
        */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL) //khi chuyển qua json, field nào null thì không cần hiển thị
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T>{
    int code = 1000; //lưu mã trả về của api. VD: 404-error, 500-.... Ta sẽ khai báo mặc định 1000: tức là request được xử lý thành công
    String message;
    T result;

}
