package com.dustess.oniondemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lixy
 * @date 2022/6/28 11:02
 * @version 1.0
 * @description
 */
@Data
@AllArgsConstructor
public class ResponseVO<T> implements Serializable {

    private int code;
    private String msg;
    private boolean success;
    private T data;


    public static ResponseVO success(Object data) {
        ResponseVO responseVO = new ResponseVO(200, "成功", true, data);
        return responseVO;
    }

    public static ResponseVO fail() {
        ResponseVO responseVO = new ResponseVO(500, "内部错误", false, null);
        return responseVO;
    }
}