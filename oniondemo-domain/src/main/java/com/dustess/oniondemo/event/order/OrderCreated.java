package com.dustess.oniondemo.event.order;

import com.dustess.oniondemo.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderCreated implements DomainEvent {
    private Long id;
    private String account;
    private Integer amount;
    private List<EventOrderItem> items;
    private LocalDateTime createdAt;

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
        return "OrderCreated";
    }

    @Override
    public String eventVersion() {
        return "20201031";
    }

    @Override
    public String account() {
        return this.account;
    }
}
