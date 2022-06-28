package com.dustess.oniondemo.entity.product;

import lombok.Data;

@Data
public class ProductStatistics {
    private Long id;
    private Long productId;
    private String account;
    private Integer paidOrderCount;
    private Integer unpaidOrderCount;
}