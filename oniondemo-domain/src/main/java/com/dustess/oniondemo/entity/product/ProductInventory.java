package com.dustess.oniondemo.entity.product;

import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.event.product.InventoryDecreased;
import com.dustess.oniondemo.event.product.InventoryIncreased;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductInventory {
    private Long id;
    private String account;
    private Long productId;
    private int quantity;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Integer version;

    public DomainEvent increase(int quantity, String updatedBy, LocalDateTime updatedAt) {
        if (quantity <= 0) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        this.quantity += quantity;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        return new InventoryIncreased(this.id, this.account, this.quantity);
    }

    public DomainEvent decrease(int quantity, String updatedBy, LocalDateTime updatedAt) {
        if (quantity <= 0) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        if (this.quantity - quantity < 0) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        this.quantity -= quantity;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        return new InventoryDecreased(this.id, this.account, this.quantity);
    }
}