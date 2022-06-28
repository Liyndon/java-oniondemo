package com.dustess.oniondemo.repository;

import com.dustess.oniondemo.entity.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    Product load(Long id);
    List<Product> loads(List<Long> ids);
    boolean isNameDuplicated(String name);
    void save(Product product);
    void update(Product product);
}
