package com.dustess.oniondemo.entity.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    Unpaid(0, "未付款"),
    Paid(1, "已付款"),
    Canceled(2, "已取消"),
    ;

    private Integer code;
    private String value;

    OrderStatus(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static OrderStatus get(Integer type){
        OrderStatus[] enums = values();
        for (OrderStatus obj : enums) {
            if (obj.getCode().equals(type)) {
                return obj;
            }
        }
        return null;
    }
}
