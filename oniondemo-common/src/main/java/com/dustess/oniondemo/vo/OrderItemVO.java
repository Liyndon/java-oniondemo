package com.dustess.oniondemo.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderItemVO {
    private ProductVO product;
    private Integer unitPrice;
    private Integer quantity;
    private Integer amount;
}
