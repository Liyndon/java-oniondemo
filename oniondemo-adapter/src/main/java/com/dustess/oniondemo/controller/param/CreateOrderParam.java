package com.dustess.oniondemo.controller.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreateOrderParam {

    @Valid
    private List<CreateOrderItemParam> items;

    @Data
    public static class CreateOrderItemParam {
        @NotBlank(message = "商品ID不能为空")
        private Long productId;

        @Min(value=1, message = "商品数量为零或负数")
        private Integer quantity;
    }
}
