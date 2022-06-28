package com.dustess.oniondemo.repository;

import com.dustess.oniondemo.entity.product.ProductStatistics;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStatisticsRepository {
    ProductStatistics load(Long id);
    void upsert(Long id, Integer paidOrderCountDelta, Integer unpaidOrderCountDelta);
}
