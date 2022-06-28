package com.dustess.oniondemo.exception;

/**
 * @author lixy
 * @date 2022/6/28 10:16
 * @version 1.0
 * @description
 */
public class BizException extends RuntimeException {


    private BizException(String message) {
        super(message);
    }

    public static BizException with(ExceptionEnum exceptionEnum) {
        return new BizException(exceptionEnum.getMessage());
    }

    public static BizException with(String message) {
        return new BizException(message);
    }
}