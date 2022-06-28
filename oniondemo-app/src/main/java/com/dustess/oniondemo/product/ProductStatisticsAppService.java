package com.dustess.oniondemo.product;

import com.dustess.oniondemo.builder.ProductVOBuilder;
import com.dustess.oniondemo.entity.order.Order;
import com.dustess.oniondemo.entity.order.OrderStatus;
import com.dustess.oniondemo.entity.product.ProductStatistics;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import com.dustess.oniondemo.repository.OrderRepository;
import com.dustess.oniondemo.repository.ProductStatisticsRepository;
import com.dustess.oniondemo.vo.ProductStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductStatisticsAppService {

    @Autowired
    private ProductStatisticsRepository productStatisticsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVOBuilder productVOBuilder;

    public void update(List<Long> productIds) {
        for (Long productId : productIds) {
            List<Order> orders = orderRepository.findByProductId(productId);
            Map<OrderStatus, Long> statusMap = orders.stream().collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
            Long paidOrderCountDelta = statusMap.getOrDefault(OrderStatus.Paid, 0L);
            Long unpaidOrderCountDelta = statusMap.getOrDefault(OrderStatus.Unpaid, 0L);
            productStatisticsRepository.upsert(productId, Math.toIntExact(paidOrderCountDelta), Math.toIntExact(unpaidOrderCountDelta));
        }
    }

    public ProductStatisticsVO get(Long id) {
        ProductStatistics productStatistics = productStatisticsRepository.load(id);
        if (productStatistics == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        return productVOBuilder.buildProductStatisticsVO(productStatistics);
    }
}
