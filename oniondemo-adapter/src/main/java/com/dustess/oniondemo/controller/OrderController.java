package com.dustess.oniondemo.controller;


import com.dustess.oniondemo.controller.param.ChangeInventoryParam;
import com.dustess.oniondemo.controller.param.CreateOrderParam;
import com.dustess.oniondemo.dto.CreateOrderDTO;
import com.dustess.oniondemo.dto.OrderItemDTO;
import com.dustess.oniondemo.order.OrderAppService;
import com.dustess.oniondemo.vo.OrderDetailVO;
import com.dustess.oniondemo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderAppService orderAppService;

    @ApiOperation(value = "获取订单详情")
    @GetMapping("/orders/{id}")
    public ResponseVO<OrderDetailVO> get(@PathVariable(value = "id") Long id) {
        return ResponseVO.success(orderAppService.get(id));
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/orders")
    public ResponseVO<OrderDetailVO> save(@RequestBody CreateOrderParam param) {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setItems(param.getItems().stream().map(i -> {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            BeanUtils.copyProperties(i, orderItemDTO);
            return orderItemDTO;
        }).collect(Collectors.toList()));
        return ResponseVO.success(orderAppService.create(dto));
    }

    @ApiOperation(value = "订单支付")
    @PostMapping("/orders/{id}/pay")
    public ResponseVO<OrderDetailVO> publish(@PathVariable(value = "id") Long id) {
        return ResponseVO.success(orderAppService.pay(id));
    }

    @ApiOperation(value = "取消订单")
    @PostMapping("/orders/{id}/cancel")
    public ResponseVO<OrderDetailVO> cancel(@PathVariable(value = "id") Long id) {
        return ResponseVO.success(orderAppService.cancel(id));
    }

    @ApiOperation(value = "取消订单")
    @PostMapping("/orders/{id}/change_quantity")
    public ResponseVO<OrderDetailVO> changeQuantity(@PathVariable(value = "id") Long id, @RequestBody ChangeInventoryParam param) {
        return ResponseVO.success(orderAppService.changeItemQuantity(id, param.getProductId(), param.getDelta()));
    }
}
