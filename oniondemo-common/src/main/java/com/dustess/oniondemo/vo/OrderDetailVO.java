package com.dustess.oniondemo.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailVO {
    private String id;
    private String orderNo;
    private UserVO customer;
    private Integer amount;
    private Integer status;
    private List<OrderItemVO> items;
}
