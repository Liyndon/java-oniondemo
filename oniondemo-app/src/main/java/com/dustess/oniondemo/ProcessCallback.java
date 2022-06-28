package com.dustess.oniondemo;

import com.dustess.oniondemo.event.DomainEvent;

public interface ProcessCallback<T> {
    DomainEvent onProcess(T entity);
}
