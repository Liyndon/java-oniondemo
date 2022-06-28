package com.dustess.oniondemo.order;

import com.dustess.oniondemo.EventPublisher;
import com.dustess.oniondemo.ProcessCallback;
import com.dustess.oniondemo.builder.OrderVOBuilder;
import com.dustess.oniondemo.dto.CreateOrderDTO;
import com.dustess.oniondemo.dto.OrderItemDTO;
import com.dustess.oniondemo.entity.CreatedResult;
import com.dustess.oniondemo.entity.order.Order;
import com.dustess.oniondemo.entity.order.OrderFactory;
import com.dustess.oniondemo.entity.product.Product;
import com.dustess.oniondemo.entity.product.ProductStatus;
import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import com.dustess.oniondemo.exception.OptimisticLockException;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.interfaces.TimeService;
import com.dustess.oniondemo.repository.OrderRepository;
import com.dustess.oniondemo.repository.ProductRepository;
import com.dustess.oniondemo.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderAppService {

    @Autowired
    private OrderFactory orderFactory;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TimeService timeService;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private ContextAccessor contextAccessor;

    @Autowired
    private OrderVOBuilder orderVOBuilder;

    @Transactional(rollbackFor = Exception.class)
    public OrderDetailVO create(CreateOrderDTO dto) {
        for (OrderItemDTO orderItemDTO : dto.getItems()) {
            Product product = productRepository.load(orderItemDTO.getProductId());
            if (product == null) {
                throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
            }
            if (product.getStatus() == ProductStatus.UnPublished) {
                throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
            }
            orderItemDTO.setUnitPrice(product.getPrice());
        }

        CreatedResult<Order> result = orderFactory.create(dto);
        Order order = result.getEntity();
        orderRepository.save(order);
        eventPublisher.publish(result.getEvent());
        return orderVOBuilder.buildOrderDetailVO(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDetailVO pay(Long productId) {
        String customerId = contextAccessor.getCustomerId();
        return loadThenDo(productId, p -> p.pay(customerId, timeService.now()));
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDetailVO cancel(Long productId) {
        String customerId = contextAccessor.getCustomerId();
        return loadThenDo(productId, p -> p.cancel(customerId, timeService.now()));
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDetailVO changeItemQuantity(Long orderId, Long productId, Integer delta) {
        return loadThenDo(orderId, p -> p.changeItemQuantity(productId, delta));
    }

    private OrderDetailVO loadThenDo(Long orderId, ProcessCallback<Order> callback) {
        Order order = orderRepository.load(orderId);
        if (order == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        DomainEvent event = callback.onProcess(order);
        try {
            orderRepository.update(order);
        } catch (OptimisticLockException e) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        eventPublisher.publish(event);
        return orderVOBuilder.buildOrderDetailVO(order);
    }

    public OrderDetailVO get(Long id) {
        Order order = orderRepository.load(id);
        if (order == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        return orderVOBuilder.buildOrderDetailVO(order);
    }
}
