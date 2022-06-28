package com.dustess.oniondemo.event.product;

import com.dustess.oniondemo.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryIncreased implements DomainEvent {
    private Long id;
    private String account;
    private int increaseQuantity;

    @Override
    public String aggregateType() {
        return "inventory";
    }

    @Override
    public String aggregateId() {
        return String.valueOf(this.id);
    }

    @Override
    public String eventType() {
        return "InventoryIncreased";
    }

    @Override
    public String eventVersion() {
        return "20211031";
    }

    @Override
    public String account() {
        return this.account;
    }
}