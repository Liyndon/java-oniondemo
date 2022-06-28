package com.dustess.oniondemo.controller.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ChangeInventoryParam {
    @NotBlank(message = "商品ID不能为空")
    private Long productId;

    @Min(value = 1, message = "库存变化量不能为零或负数")
    private Integer delta;
}
