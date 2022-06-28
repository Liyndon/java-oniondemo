package com.dustess.oniondemo.entity;

import com.dustess.oniondemo.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatedResult<T> {
    private T entity;
    private DomainEvent event;
}
