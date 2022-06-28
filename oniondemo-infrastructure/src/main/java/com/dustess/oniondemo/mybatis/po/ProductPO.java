package com.dustess.oniondemo.mybatis.po;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class ProductPO {
    private Long id;

    /**
    * 租户ID
    */
    private String account;

    /**
    * 商品名称
    */
    private String name;

    /**
    * 商品描述
    */
    private String description;

    /**
    * 商品价格，单位为分
    */
    private Integer price;

    /**
    * 状态：0未上架、1已上架
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
    * 更新时间
    */
    private LocalDateTime updatedAt;

    /**
    * 更新人
    */
    private Long updatedBy;

    /**
    * 乐观锁版本号
    */
    private Integer version;
}