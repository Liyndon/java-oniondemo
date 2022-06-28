package com.dustess.oniondemo;

import com.dustess.oniondemo.event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
