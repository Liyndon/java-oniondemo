package com.dustess.oniondemo.entity.order;


import com.dustess.oniondemo.event.order.EventOrderItem;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderItem {
    private Long id;
    private Long productId;
    private Integer unitPrice;
    private Integer quantity;

    public Integer getTotalPrice() {
        return this.unitPrice * this.quantity;
    }

    public void changeQuantity(Integer delta) {
        if (this.quantity + delta <= 0) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        this.quantity = this.quantity + delta;
    }

    public EventOrderItem toEventOrderItem() {
        EventOrderItem item = new EventOrderItem();
        BeanUtils.copyProperties(this, item);
        item.setItemId(this.id);
        return item;
    }

    public static List<EventOrderItem> toEventOrderItems(List<OrderItem> items) {
        return items.stream().map(OrderItem::toEventOrderItem).collect(Collectors.toList());
    }
}
