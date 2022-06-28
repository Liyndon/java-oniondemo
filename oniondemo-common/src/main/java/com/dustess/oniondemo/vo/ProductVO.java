package com.dustess.oniondemo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVO {
    private String id;
    private String name;
    private String description;
    private Integer price;
    private Integer status;
}
