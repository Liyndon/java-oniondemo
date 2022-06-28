package com.dustess.oniondemo.persistence;


import com.dustess.oniondemo.entity.order.Order;
import com.dustess.oniondemo.entity.order.OrderItem;
import com.dustess.oniondemo.entity.order.OrderStatus;
import com.dustess.oniondemo.exception.OptimisticLockException;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.mybatis.mapper.OrderItemMapper;
import com.dustess.oniondemo.mybatis.mapper.OrderMapper;
import com.dustess.oniondemo.mybatis.po.OrderItemPO;
import com.dustess.oniondemo.mybatis.po.OrderPO;
import com.dustess.oniondemo.repository.OrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MybatisOrderRepository implements OrderRepository {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ContextAccessor contextAccessor;

    @Override
    public Order load(Long id) {
        if (id == null) {
            return null;
        }
        List<Order> orders = loads(List.of(id));
        if (orders.isEmpty()) {
            return null;
        }
        return orders.get(0);
    }

    @Override
    public List<Order> loads(List<Long> ids) {
        String account = contextAccessor.getAccount();
        List<OrderPO> orderPOList = orderMapper.selectByIds(account, ids);
        List<Order> result = new ArrayList<>();
        for (OrderPO orderPO : orderPOList) {
            Order order = new Order();
            order.setId(orderPO.getId());
            order.setAccount(orderPO.getAccount());
            order.setOrderNo(orderPO.getOrderNo());
            order.setCustomerId(String.valueOf(orderPO.getCustomerId()));
            order.setAmount(orderPO.getAmount());
            order.setStatus(OrderStatus.get(Integer.valueOf(orderPO.getStatus())));
            order.setCreatedAt(orderPO.getCreatedAt());
            order.setCreatedBy(String.valueOf(orderPO.getCreatedBy()));
            order.setUpdatedAt(orderPO.getUpdatedAt());
            order.setUpdatedBy(String.valueOf(orderPO.getUpdatedBy()));
            order.setVersion(orderPO.getVersion());

            List<OrderItemPO> orderItemPOList = orderItemMapper.selectByOrderId(account, orderPO.getId());
            List<OrderItem> items = orderItemPOList.stream().map(i -> {
                OrderItem orderItem = new OrderItem();
                BeanUtils.copyProperties(i, orderItem);
                return orderItem;
            }).collect(Collectors.toList());
            order.setItems(items);

            result.add(order);
        }
        return result;
    }

    @Override
    public void save(Order order) {
        OrderPO orderPO = new OrderPO();
        orderPO.setId(order.getId());
        orderPO.setAccount(order.getAccount());
        orderPO.setOrderNo(order.getOrderNo());
        orderPO.setCustomerId(Long.valueOf(order.getCustomerId()));
        orderPO.setAmount(order.getAmount());
        orderPO.setStatus((byte) order.getStatus().getCode().intValue());
        orderPO.setCreatedAt(order.getCreatedAt());
        orderPO.setCreatedBy(Long.valueOf(order.getCreatedBy()));
        orderPO.setUpdatedAt(order.getUpdatedAt());
        orderPO.setUpdatedBy(Long.valueOf(order.getUpdatedBy()));
        orderPO.setVersion(order.getVersion());

        List<OrderItemPO> poList = order.getItems().stream().map(i -> {
            OrderItemPO orderItem = new OrderItemPO();
            BeanUtils.copyProperties(i, orderItem);
            return orderItem;
        }).collect(Collectors.toList());
        for (OrderItemPO po : poList) {
            po.setOrderId(order.getId());
            po.setAccount(order.getAccount());
            orderItemMapper.insert(po);
        }
        orderMapper.insert(orderPO);
    }

    @Override
    public void update(Order order) {
        OrderPO orderPO = new OrderPO();
        orderPO.setId(order.getId());
        orderPO.setAccount(order.getAccount());
        orderPO.setOrderNo(order.getOrderNo());
        orderPO.setCustomerId(Long.valueOf(order.getCustomerId()));
        orderPO.setAmount(order.getAmount());
        orderPO.setStatus((byte) order.getStatus().getCode().intValue());
        orderPO.setUpdatedAt(order.getUpdatedAt());
        orderPO.setUpdatedBy(Long.valueOf(order.getUpdatedBy()));
        orderPO.setVersion(order.getVersion());

        orderItemMapper.deleteByOrderId(order.getAccount(), order.getId());
        List<OrderItemPO> poList = order.getItems().stream().map(i -> {
            OrderItemPO orderItem = new OrderItemPO();
            BeanUtils.copyProperties(i, orderItem);
            return orderItem;
        }).collect(Collectors.toList());
        for (OrderItemPO po : poList) {
            po.setOrderId(order.getId());
            po.setAccount(order.getAccount());
            orderItemMapper.insert(po);
        }
        int rowsAffected = orderMapper.updateByIdAndVersion(orderPO);
        if (rowsAffected == 0) {
            throw new OptimisticLockException();
        }
        order.setVersion(order.getVersion() + 1);
    }

    @Override
    public List<Order> findByProductId(Long productId) {
        String account = contextAccessor.getAccount();

        List<OrderItemPO> items = orderItemMapper.selectByProductId(account, productId);
        List<Long> orderIds = items.stream().map(OrderItemPO::getOrderId).collect(Collectors.toList());
        return loads(orderIds);
    }
}
