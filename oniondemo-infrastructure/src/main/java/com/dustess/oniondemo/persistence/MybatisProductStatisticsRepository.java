package com.dustess.oniondemo.persistence;

import com.dustess.oniondemo.entity.product.ProductStatistics;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.mybatis.mapper.ProductStatisticsMapper;
import com.dustess.oniondemo.mybatis.po.ProductStatisticsPO;
import com.dustess.oniondemo.repository.ProductStatisticsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class MybatisProductStatisticsRepository implements ProductStatisticsRepository {

    @Resource
    private ProductStatisticsMapper productStatisticsMapper;

    @Autowired
    private ContextAccessor contextAccessor;

    @Override
    public ProductStatistics load(Long id) {
        if (id == null) {
            return null;
        }

        String account = contextAccessor.getAccount();
        ProductStatisticsPO po = productStatisticsMapper.selectById(account, id);
        ProductStatistics productStatistics = new ProductStatistics();
        BeanUtils.copyProperties(po, productStatistics);
        return productStatistics;
    }

    @Override
    public void upsert(Long id, Integer paidOrderCountDelta, Integer unpaidOrderCountDelta) {
        String account = contextAccessor.getAccount();
        ProductStatisticsPO po = new ProductStatisticsPO();
        po.setId(id);
        po.setAccount(account);
        po.setProductId(id);
        po.setPaidOrderCount(paidOrderCountDelta);
        po.setUnpaidOrderCount(unpaidOrderCountDelta);
        productStatisticsMapper.replace(po);
    }
}
