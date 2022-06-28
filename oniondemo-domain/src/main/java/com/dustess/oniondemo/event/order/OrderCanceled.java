package com.dustess.oniondemo.event.order;

import com.dustess.oniondemo.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCanceled implements DomainEvent {
    private Long id;
    private String account;

    @Override
    public String aggregateType() {
        return "order";
    }

    @Override
    public String aggregateId() {
        return String.valueOf(this.id);
    }

    @Override
    public String eventType() {
        return "OrderCanceled";
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
