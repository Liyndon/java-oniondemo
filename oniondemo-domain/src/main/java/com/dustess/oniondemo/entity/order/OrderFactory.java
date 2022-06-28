package com.dustess.oniondemo.entity.order;


import com.dustess.oniondemo.constants.DomainConstants;
import com.dustess.oniondemo.dto.CreateOrderDTO;
import com.dustess.oniondemo.entity.CreatedResult;
import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.event.order.OrderCreated;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.interfaces.IDGenerator;
import com.dustess.oniondemo.interfaces.OrderNoGenerator;
import com.dustess.oniondemo.interfaces.TimeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class OrderFactory {

    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private OrderNoGenerator orderNoGenerator;

    @Autowired
    private ContextAccessor contextAccessor;

    @Autowired
    private TimeService timeService;

    public CreatedResult<Order> create(CreateOrderDTO dto) {
        Long orderId = idGenerator.nextId();
        String orderNo = orderNoGenerator.nextNo();
        String customerId = contextAccessor.getCustomerId();
        String account = contextAccessor.getAccount();
        LocalDateTime createdAt = timeService.now();

        Order order = new Order();
        order.setId(orderId);
        order.setAccount(account);
        order.setOrderNo(orderNo);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.Unpaid);
        order.setCreatedAt(createdAt);
        order.setCreatedBy(customerId);
        order.setUpdatedAt(createdAt);
        order.setUpdatedBy(customerId);
        order.setVersion(DomainConstants.EntityInitialVersion);
        order.setItems(dto.getItems().stream().map(i -> {
            OrderItem item = new OrderItem();
            BeanUtils.copyProperties(i, item);
            item.setId(idGenerator.nextId());
            return item;
        }).collect(Collectors.toList()));
        order.recalculate();

        DomainEvent event = new OrderCreated(
                order.getId(),
                order.getAccount(),
                order.getAmount(),
                OrderItem.toEventOrderItems(order.getItems()),
                order.getCreatedAt()
        );

        return new CreatedResult<>(order, event);
    }
}
