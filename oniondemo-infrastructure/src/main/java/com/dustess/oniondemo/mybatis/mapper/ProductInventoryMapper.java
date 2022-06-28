package com.dustess.oniondemo.mybatis.mapper;

import com.dustess.oniondemo.mybatis.po.ProductInventoryPO;
import org.apache.ibatis.annotations.Param;

public interface ProductInventoryMapper {
    int insert(ProductInventoryPO record);

    int updateByIdAndVersion(ProductInventoryPO record);

    ProductInventoryPO selectById(@Param("account") String account, @Param("id") Long id);
}