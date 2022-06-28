package com.dustess.oniondemo.controller.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class CreateProductParam {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotBlank(message = "商品描述不能为空")
    private String description;

    @Min(value=1, message = "商品价格不能为零或负数")
    private Integer price;
}
