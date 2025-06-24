package com.zosh.zosh_social_youtube.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001,"Invalid message key",HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002,"User existed",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"User not existed",HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003,"Username must be at least 3 characters",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004,"Password must be at least 8 charaterers",HttpStatus.BAD_REQUEST),
    CAPTION_INVALID(1008,"The caption must not exceed 500 characters",HttpStatus.BAD_REQUEST),
    //PASSWORD_INVALID(1009,"Password must be at least 8 charaterers",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006,"Unauthenticated",HttpStatus.UNAUTHORIZED),
    POST_NOT_FOUND(1007, "Post not existed",HttpStatus.NOT_FOUND),
    POST_ALREADY_SAVED(1009,"Post is already saved",HttpStatus.CONFLICT),
    UNAUTHORIZED_ACTION(1010,"Unauthorized action",HttpStatus.UNAUTHORIZED),
    EMAIL_REQUIRED(1011,"Email is required",HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1012,"Invalid email",HttpStatus.BAD_REQUEST),
    INVALID_DOB(1013,"Invalid date",HttpStatus.BAD_REQUEST),
    INVALID_ROLE(1014,"Invalid role",HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1015,"Internal server error",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(1016,"Invalid request",HttpStatus.BAD_REQUEST),
    DATABASE_ERROR(1017,"Database error",HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(1018,"Unauthorized",HttpStatus.UNAUTHORIZED),
    ALREADY_FOLLOWING(1019,"Already following",HttpStatus.CONFLICT),
    COMMENT_NOT_FOUND(1020,"Comment not found",HttpStatus.NOT_FOUND),
    CHAT_NOT_FOUND(1021, "Chat not found",HttpStatus.NOT_FOUND),
    UNAUTHORIZED_ACCESS(1022,"Unauthorized access",HttpStatus.UNAUTHORIZED),
    NOT_FOLLOWING(1023, "Not following", HttpStatus.NOT_FOUND),
    POST_NOT_SAVED_YET(1024,"Not saved yet",HttpStatus.NOT_FOUND);

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
