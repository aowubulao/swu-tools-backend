package com.neoniou.swu.exception;

/**
 * @author neo.zzj
 */
public enum ExceptionEnum {

    /**
     *
     */
    LOGIN_FAIL(400, "用户名或密码错误！"),
    NETWORK_ERROR(500, "服务器网络错误！"),
    ;
    private int statusCode;
    private String message;

    ExceptionEnum() {
    }

    ExceptionEnum(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}