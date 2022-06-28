package com.dustess.oniondemo.consumer;

import com.dustess.oniondemo.product.ProductStatisticsAppService;
import com.dustess.oniondemo.consumer.event.OrderBaseEvent;
import com.dustess.oniondemo.entity.order.Order;
import com.dustess.oniondemo.repository.OrderRepository;
import com.dustess.oniondemo.service.context.ContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductStatisticsAppService productStatisticsAppService;

    @KafkaListener(id = "oniondemo", topics = "oniondemo_domain_event")
    public void listen(String msgData, Acknowledgment acknowledgment) throws JsonProcessingException {
        // TODO 消息处理路由，Context统一注入，这块写的还比较挫
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(msgData);
        String eventType = node.get("eventType").textValue();
        String eventStr = node.get("event").toString();
        String account = node.get("account").textValue();
        ContextHolder.setAccount(account);

        try {
            if (eventType.equals("OrderPaid") || eventType.equals("OrderCreated") || eventType.equals("OrderItemChanged")) {
                OrderBaseEvent event = mapper.readValue(eventStr, OrderBaseEvent.class);
                Order order = orderRepository.load(event.getId());
                List<Long> productIds = order.getProductIds();
                productStatisticsAppService.update(productIds);
                log.info("Message received:{}", msgData);
            } else {
                log.info("Message ignored:{}", msgData);
            }
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Consume failed:{}, ex:{}", msgData, e);
            throw e;
        }
    }
}
