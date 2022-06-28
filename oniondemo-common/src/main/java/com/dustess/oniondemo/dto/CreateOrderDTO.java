package com.dustess.oniondemo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    private List<OrderItemDTO> items;
}
