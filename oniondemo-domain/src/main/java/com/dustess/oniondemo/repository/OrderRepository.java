package com.dustess.oniondemo.repository;

import com.dustess.oniondemo.entity.order.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {
    Order load(Long id);
    List<Order> loads(List<Long> ids);
    void save(Order product);
    void update(Order product);
    List<Order> findByProductId(Long productId);
}
