package com.dustess.oniondemo.persistence;

import com.dustess.oniondemo.exception.OptimisticLockException;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.entity.product.Product;
import com.dustess.oniondemo.entity.product.ProductStatus;
import com.dustess.oniondemo.mybatis.mapper.ProductMapper;
import com.dustess.oniondemo.mybatis.po.ProductPO;
import com.dustess.oniondemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MybatisProductRepository implements ProductRepository {

    @Resource
    private ProductMapper productMapper;

    @Autowired
    private ContextAccessor contextAccessor;

    @Override
    public Product load(Long id) {
        if (id == null) {
            return null;
        }
        List<Product> products = loads(List.of(id));
        if (products.isEmpty()) {
            return null;
        }
        return products.get(0);
    }

    @Override
    public List<Product> loads(List<Long> ids) {
        String account = contextAccessor.getAccount();
        List<ProductPO> productPOList = productMapper.selectByIds(account, ids);
        List<Product> result = new ArrayList<>();
        for (ProductPO productPO : productPOList) {
            Product product = new Product();
            product.setId(productPO.getId());
            product.setAccount(productPO.getAccount());
            product.setName(productPO.getName());
            product.setDescription(productPO.getDescription());
            product.setPrice(productPO.getPrice());
            product.setStatus(ProductStatus.get(Integer.valueOf(productPO.getStatus())));
            product.setCreatedAt(productPO.getCreatedAt());
            product.setCreatedBy(String.valueOf(productPO.getCreatedBy()));
            product.setUpdatedAt(productPO.getUpdatedAt());
            product.setUpdatedBy(String.valueOf(productPO.getUpdatedBy()));
            product.setVersion(productPO.getVersion());
            result.add(product);
        }
        return result;
    }

    @Override
    public boolean isNameDuplicated(String name) {
        String account = contextAccessor.getAccount();
        ProductPO exists = productMapper.selectByName(account, name);
        return exists != null;
    }

    @Override
    public void save(Product product) {
        ProductPO productPO = new ProductPO();
        productPO.setId(product.getId());
        productPO.setAccount(product.getAccount());
        productPO.setName(product.getName());
        productPO.setDescription(product.getDescription());
        productPO.setPrice(product.getPrice());
        productPO.setStatus((byte)product.getStatus().getCode().intValue());
        productPO.setCreatedAt(product.getCreatedAt());
        productPO.setCreatedBy(Long.valueOf(product.getCreatedBy()));
        productPO.setUpdatedAt(product.getUpdatedAt());
        productPO.setUpdatedBy(Long.valueOf(product.getUpdatedBy()));
        productPO.setVersion(product.getVersion());

        productMapper.insert(productPO);
    }

    @Override
    public void update(Product product) {
        ProductPO productPO = new ProductPO();
        productPO.setId(product.getId());
        productPO.setAccount(product.getAccount());
        productPO.setName(product.getName());
        productPO.setDescription(product.getDescription());
        productPO.setPrice(product.getPrice());
        productPO.setStatus((byte)product.getStatus().getCode().intValue());
        productPO.setUpdatedAt(product.getUpdatedAt());
        productPO.setUpdatedBy(Long.valueOf(product.getUpdatedBy()));
        productPO.setVersion(product.getVersion());

        int rowsAffected = productMapper.updateByIdAndVersion(productPO);
        if (rowsAffected == 0) {
            throw new OptimisticLockException();
        }
        product.setVersion(product.getVersion() + 1);
    }
}
