package com.dustess.oniondemo.dto;

import lombok.Data;

@Data
public class CreateProductDTO {
    private String name;
    private String description;
    private Integer price;
}
