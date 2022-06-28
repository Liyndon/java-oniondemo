package com.dustess.oniondemo.mybatis.mapper;

import com.dustess.oniondemo.mybatis.po.OrderPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int insert(OrderPO record);

    int updateByIdAndVersion(OrderPO record);

    OrderPO selectById(@Param("account") String account, @Param("id") Long id);

    List<OrderPO> selectByIds(@Param("account") String account, @Param("ids") List<Long> ids);
}