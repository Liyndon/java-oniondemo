package com.dustess.oniondemo.event.product;

import com.dustess.oniondemo.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProductCreated implements DomainEvent {
    private Long id;
    private String account;
    private String name;
    private Integer price;
    private String description;
    private LocalDateTime createdAt;

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
        return "ProductCreated";
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