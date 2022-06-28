package com.dustess.oniondemo.builder;

import com.dustess.oniondemo.interfaces.UserService;
import com.dustess.oniondemo.entity.user.UserInfo;
import com.dustess.oniondemo.entity.product.Product;
import com.dustess.oniondemo.entity.product.ProductInventory;
import com.dustess.oniondemo.entity.product.ProductStatistics;
import com.dustess.oniondemo.repository.ProductInventoryRepository;
import com.dustess.oniondemo.repository.ProductRepository;
import com.dustess.oniondemo.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductVOBuilder {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private UserService userService;

    public ProductVO buildProductVO(Product product) {
        return ProductVO.builder().id(String.valueOf(product.getId()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus().getCode())
                .build();
    }

    public ProductDetailVO buildProductDetailVO(Product product) {
        ProductInventory inventory = productInventoryRepository.load(product.getId());
        UserInfo userInfo = userService.get(product.getCreatedBy());

        ProductDetailVO result = ProductDetailVO.builder().id(String.valueOf(product.getId()))
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .status(product.getStatus().getCode())
                .build();

        if (inventory != null) {
            ProductInventoryVO inventoryVO = ProductInventoryVO.builder().id(String.valueOf(inventory.getId()))
                    .name(product.getName()).quantity(inventory.getQuantity()).build();
            result.setInventory(inventoryVO);
        }
        if (userInfo != null) {
            UserVO userVO = new UserVO(userInfo.getId(), userInfo.getName());
            result.setCreatedBy(userVO);
        }
        return result;
    }

    public ProductStatisticsVO buildProductStatisticsVO(ProductStatistics productStatistics) {
        Product product = productRepository.load(productStatistics.getProductId());
        ProductStatisticsVO result = new ProductStatisticsVO();
        result.setId(String.valueOf(productStatistics.getId()));
        result.setProduct(buildProductDetailVO(product));
        result.setPaid(productStatistics.getPaidOrderCount());
        result.setUnpaid(productStatistics.getUnpaidOrderCount());
        return result;
    }
}
