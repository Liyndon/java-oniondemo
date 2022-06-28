package com.dustess.oniondemo.mybatis.po;

import java.util.Date;
import lombok.Data;

@Data
public class ProductStatisticsPO {
    private Long id;

    /**
    * 租户ID
    */
    private String account;

    /**
    * 商品ID
    */
    private Long productId;

    /**
    * 已付款订单数
    */
    private Integer paidOrderCount;

    /**
    * 未付款订单数
    */
    private Integer unpaidOrderCount;
}