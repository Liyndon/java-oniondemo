package com.dustess.oniondemo.builder;

import com.dustess.oniondemo.interfaces.UserService;
import com.dustess.oniondemo.entity.user.UserInfo;
import com.dustess.oniondemo.entity.order.Order;
import com.dustess.oniondemo.entity.order.OrderItem;
import com.dustess.oniondemo.entity.product.Product;
import com.dustess.oniondemo.repository.ProductRepository;
import com.dustess.oniondemo.vo.OrderDetailVO;
import com.dustess.oniondemo.vo.OrderItemVO;
import com.dustess.oniondemo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderVOBuilder {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVOBuilder productVOBuilder;

    public OrderDetailVO buildOrderDetailVO(Order order) {
        List<Long> productIds = order.getItems().stream().map(OrderItem::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.loads(productIds);
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Product -> Product));
        UserInfo customer = userService.get(order.getCustomerId());

        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setId(String.valueOf(order.getId()));
        orderDetailVO.setOrderNo(order.getOrderNo());
        orderDetailVO.setAmount(order.getAmount());
        orderDetailVO.setStatus(order.getStatus().getCode());
        orderDetailVO.setItems(new ArrayList<>());
        if (customer != null) {
            orderDetailVO.setCustomer(new UserVO(customer.getId(), customer.getName()));
        }

        List<OrderItemVO> orderItemVoList = new ArrayList<>();
        for (OrderItem item: order.getItems()) {
            OrderItemVO orderItemVO = new OrderItemVO();
            orderItemVO.setQuantity(item.getQuantity());
            orderItemVO.setUnitPrice(item.getUnitPrice());
            orderItemVO.setAmount(item.getTotalPrice());
            Product product = productMap.get(item.getProductId());
            if (product != null) {
                orderItemVO.setProduct(productVOBuilder.buildProductVO(product));
            }
            orderItemVoList.add(orderItemVO);
        }
        orderDetailVO.setItems(orderItemVoList);
        return orderDetailVO;
    }
}
