package com.dustess.oniondemo.event.order;

import com.dustess.oniondemo.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderPaid implements DomainEvent {
    private Long id;
    private String account;
    private String paidBy;
    private List<EventOrderItem> items;
    private LocalDateTime paidAt;

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
        return "OrderPaid";
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
