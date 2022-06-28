package com.dustess.oniondemo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInventoryVO {
    private String id;
    private String name;
    private Integer quantity;
}
