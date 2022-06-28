package com.dustess.oniondemo.repository;

import com.dustess.oniondemo.entity.product.ProductInventory;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInventoryRepository {
    ProductInventory load(Long id);
    void save(ProductInventory product);
    void update(ProductInventory product);
}
