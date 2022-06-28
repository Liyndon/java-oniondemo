package com.dustess.oniondemo.mybatis.mapper;

import com.dustess.oniondemo.mybatis.po.ProductStatisticsPO;
import org.apache.ibatis.annotations.Param;

public interface ProductStatisticsMapper {
    int replace(ProductStatisticsPO record);

    ProductStatisticsPO selectById(@Param("account") String account, @Param("id") Long id);
}