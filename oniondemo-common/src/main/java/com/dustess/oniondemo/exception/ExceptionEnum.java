package com.dustess.oniondemo.exception;

public enum ExceptionEnum {

    LOGIN_REQUIRED(10001, "请先登录"),
    ;

    private final int code;
    private final String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
