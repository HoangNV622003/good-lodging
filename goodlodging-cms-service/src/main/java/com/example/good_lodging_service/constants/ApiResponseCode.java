package com.example.good_lodging_service.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ApiResponseCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    SUCCESS(1000,"SUCCESS",HttpStatus.OK),
    INVALID_KEY(1001, "Invalid Key", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND(1002, "Entity not found", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "User not found", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(1004, "User already exists", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1005, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1006, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD(1007, "Incorrect Password", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1010, "Role not found", HttpStatus.BAD_REQUEST),
    ROLE_ALREADY_EXISTS(1011, "Role already exists", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED(1012, "Permission denied",HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_ALLOWED(1013, "Permission not allowed", HttpStatus.BAD_REQUEST),
    PERMISSION_ALREADY_EXISTS(1014, "Permission already exists", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1015, "Permission not found", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1016, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    EMAIL_OR_PHONE_NUMBER_ALREADY_EXISTS(1001,"Email or Phone Number Already Exists",HttpStatus.BAD_REQUEST),

    //AUTH
    INVALID_CREDENTIALS(2001,"Tên đăng nhập hoặc mật khẩu không đúng",HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(2002,"Mật khẩu không chính xác",HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(2003,"Tài khoản đã bị khóa",HttpStatus.BAD_REQUEST),
    ACCOUNT_DISABLED(2004,"Tài khoản đã bị vô hiệu hóa",HttpStatus.BAD_REQUEST),
    ACCOUNT_EXPIRED(2005,"Tài khoản đã hết hạn",HttpStatus.BAD_REQUEST),
    PASSWORD_EXPIRED(2006,"Mật khẩu đã hết hạn, vui lòng đổi mật khẩu",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(2007, "Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2008, "You do not have permission", HttpStatus.FORBIDDEN),
    PASSWORD_CHANGED_SUCCESSFULLY(2009,"Mật khẩu đã được thay đổi thành công",HttpStatus.OK),
    USER_DELETED_SUCCESSFULLY(2010,"Người dùng đã được xóa thành công",HttpStatus.OK),
    ;
    private final Integer code;
    private final String message;
    private final HttpStatusCode statusCode;
    ApiResponseCode(Integer code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
