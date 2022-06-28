package com.dustess.oniondemo.mybatis.po;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class OrderPO {
    private Long id;

    /**
    * 租户ID
    */
    private String account;

    /**
    * 订单号
    */
    private String orderNo;

    /**
    * 客户ID
    */
    private Long customerId;

    /**
    * 总金额
    */
    private Integer amount;

    /**
    * 状态：1未付款、2已付款
    */
    private Byte status;

    /**
    * 创建时间
    */
    private LocalDateTime createdAt;

    /**
    * 创建人
    */
    private Long createdBy;

    /**
    * 修改时间
    */
    private LocalDateTime updatedAt;

    /**
    * 修改人
    */
    private Long updatedBy;

    /**
    * 乐观锁版本号
    */
    private Integer version;
}