package com.dustess.oniondemo.mybatis.mapper;

import com.dustess.oniondemo.mybatis.po.ProductPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int insert(ProductPO record);

    int updateByIdAndVersion(ProductPO record);

    ProductPO selectById(@Param("account") String account, @Param("id") Long id);

    List<ProductPO> selectByIds(@Param("account") String account, @Param("ids") List<Long> ids);

    ProductPO selectByName(@Param("account") String account, @Param("name") String name);
}