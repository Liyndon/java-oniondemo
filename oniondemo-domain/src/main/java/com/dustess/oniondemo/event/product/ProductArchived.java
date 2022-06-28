package com.dustess.oniondemo.event.product;

import com.dustess.oniondemo.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProductArchived implements DomainEvent {
    private Long id;
    private String account;
    private String updatedBy;
    private LocalDateTime updatedAt;

    @Override
    public String aggregateType() {
        return "product";
    }

    @Override
    public String aggregateId() {
        return String.valueOf(this.id);
    }

    @Override
    public String eventType() {
        return "ProductArchived";
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