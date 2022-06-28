package com.dustess.oniondemo.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private Integer unitPrice;
    private Integer quantity;
}
