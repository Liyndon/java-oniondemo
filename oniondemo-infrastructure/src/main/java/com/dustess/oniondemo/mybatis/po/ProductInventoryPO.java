package com.dustess.oniondemo.mybatis.po;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ProductInventoryPO {
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
     * 数量
     */
    private Integer quantity;

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