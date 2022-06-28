package com.dustess.oniondemo.event.order;

import lombok.Data;

@Data
public class EventOrderItem {
    private Long itemId;
    private Long productId;
    private Integer unitPrice;
}
