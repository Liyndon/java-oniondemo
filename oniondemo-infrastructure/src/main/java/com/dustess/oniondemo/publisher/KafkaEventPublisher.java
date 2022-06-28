package com.dustess.oniondemo.publisher;


import cn.hutool.json.JSONUtil;
import com.dustess.oniondemo.EventPublisher;
import com.dustess.oniondemo.event.DomainEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Data
class KafkaEventBody {
    private DomainEvent event;
    private String aggregateType;
    private String aggregateId;
    private String eventType;
    private String eventVersion;
    private String account;
}

@Component
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.points.obtain}")
    private String topic;

    @Override
    public void publish(DomainEvent event) {
        if (event == null) {
            return;
        }

        KafkaEventBody body = new KafkaEventBody();

        body.setEvent(event);
        body.setAggregateId(event.aggregateId());
        body.setAggregateType(event.aggregateType());
        body.setEventType(event.eventType());
        body.setEventVersion(event.eventVersion());
        body.setAccount(event.account());
        String message = JSONUtil.toJsonStr(body);
        kafkaTemplate.send(topic, event.aggregateId(), message);
        log.info("Kafka Domain Event Send: " + message);
    }
}
