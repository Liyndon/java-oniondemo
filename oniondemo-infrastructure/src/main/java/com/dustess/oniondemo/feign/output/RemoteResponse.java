package com.dustess.oniondemo.feign.output;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RemoteResponse<T> {

    public final static String SUCCESS_CODE = "500200";

    public static RemoteResponse EMPTY = new RemoteResponse(null, "服务器繁忙", false, null);

    private String code;

    private String msg;

    private boolean success;

    private T data;

    public Boolean isSuccessful() {
        return "500200".equals(code);
    }

}

