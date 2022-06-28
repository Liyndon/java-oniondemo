package com.dustess.oniondemo.mybatis.mapper;

import com.dustess.oniondemo.mybatis.po.OrderItemPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int insert(OrderItemPO record);

    int update(OrderItemPO record);

    int deleteByOrderId(@Param("account") String account, @Param("orderId") Long orderId);

    List<OrderItemPO> selectByOrderId(@Param("account") String account, @Param("orderId") Long orderId);

    List<OrderItemPO> selectByProductId(@Param("account") String account, @Param("productId") Long productId);
}