package com.dustess.oniondemo.publisher;

import com.dustess.oniondemo.EventPublisher;
import com.dustess.oniondemo.event.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

class DomainApplicationEvent extends ApplicationEvent {

    private final DomainEvent event;

    public DomainApplicationEvent(Object source, DomainEvent event) {
        super(source);
        this.event = event;
    }

    public DomainEvent getEvent() {
        return this.event;
    }
}

@Component
class SpringEventListener {

    @Autowired
    private KafkaEventPublisher kafkaEventPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onApplicationEvent(DomainApplicationEvent applicationEvent) {
        kafkaEventPublisher.publish(applicationEvent.getEvent());
    }
}

@Primary
@Component
public class SpringEventPublisher implements EventPublisher {

    @Autowired
    private ApplicationContext context;

    @Override
    public void publish(DomainEvent event) {
        context.publishEvent(new DomainApplicationEvent(this, event));
    }
}
