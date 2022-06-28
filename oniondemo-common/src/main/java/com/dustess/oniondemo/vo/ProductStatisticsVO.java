package com.dustess.oniondemo.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductStatisticsVO {
    private String id;
    private ProductDetailVO product;
    private Integer paid;
    private Integer unpaid;
}
