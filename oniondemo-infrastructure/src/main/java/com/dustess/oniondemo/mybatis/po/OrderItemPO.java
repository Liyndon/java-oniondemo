package com.dustess.oniondemo.mybatis.po;

import lombok.Data;

@Data
public class OrderItemPO {
    private Long id;

    /**
     * 租户ID
     */
    private String account;

    /**
    * 订单ID
    */
    private Long orderId;

    /**
    * 商品ID
    */
    private Long productId;

    /**
    * 单价，单位分
    */
    private Integer unitPrice;

    /**
    * 数量
    */
    private Integer quantity;
}