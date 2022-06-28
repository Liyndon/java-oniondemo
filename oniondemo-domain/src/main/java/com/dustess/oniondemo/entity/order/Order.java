package com.dustess.oniondemo.entity.order;

import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.event.order.OrderCanceled;
import com.dustess.oniondemo.event.order.OrderItemChanged;
import com.dustess.oniondemo.event.order.OrderPaid;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Order {
    private Long id;
    private String account;
    private String orderNo;
    private String customerId;
    private Integer amount;
    private OrderStatus status;
    private List<OrderItem> items;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Integer version;

    public DomainEvent pay(String paidBy, LocalDateTime paidAt) {
        if (this.status == OrderStatus.Paid) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        } else if (this.status == OrderStatus.Canceled) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        if (!paidBy.equals(this.customerId)) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        this.status = OrderStatus.Paid;
        this.updatedAt = paidAt;
        this.updatedBy = paidBy;
        return new OrderPaid(this.id, this.account, this.updatedBy, OrderItem.toEventOrderItems(this.items), this.updatedAt);
    }

    public DomainEvent cancel(String userId, LocalDateTime canceledAt) {
        if (this.status == OrderStatus.Paid) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        } else if (this.status == OrderStatus.Canceled) {
            return null;
        }
        this.status = OrderStatus.Canceled;
        this.updatedAt = canceledAt;
        this.updatedBy = userId;
        return new OrderCanceled(this.id, this.account);
    }

    public DomainEvent changeItemQuantity(Long productId, Integer delta) {
        OrderItem exist = findExistItem(productId);
        if (exist == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        exist.changeQuantity(delta);
        this.recalculate();

        return new OrderItemChanged(this.id, this.account, this.amount, OrderItem.toEventOrderItems(this.items));
    }

    private OrderItem findExistItem(Long productId) {
        for (OrderItem item : this.items) {
            if (productId.equals(item.getProductId())) {
                return item;
            }
        }
        return null;
    }

    public void recalculate() {
        Integer amount = 0;
        for (OrderItem item : this.items) {
            amount += item.getTotalPrice();
        }
        this.amount = amount;
    }

    public List<Long> getProductIds() {
        return this.items.stream().map(OrderItem::getProductId).collect(Collectors.toList());
    }
}
