package com.dustess.oniondemo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailVO {
    private String id;
    private String name;
    private String description;
    private Integer price;
    private Integer status;
    private UserVO createdBy;
    private ProductInventoryVO inventory;
}
