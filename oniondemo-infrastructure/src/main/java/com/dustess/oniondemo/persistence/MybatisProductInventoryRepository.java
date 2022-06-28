package com.dustess.oniondemo.persistence;

import com.dustess.oniondemo.constants.DomainConstants;
import com.dustess.oniondemo.exception.OptimisticLockException;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.entity.product.ProductInventory;
import com.dustess.oniondemo.mybatis.mapper.ProductInventoryMapper;
import com.dustess.oniondemo.mybatis.po.ProductInventoryPO;
import com.dustess.oniondemo.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class MybatisProductInventoryRepository implements ProductInventoryRepository {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private ContextAccessor contextAccessor;

    @Override
    public ProductInventory load(Long id) {
        String account = contextAccessor.getAccount();
        ProductInventoryPO inventoryPO = productInventoryMapper.selectById(account, id);
        ProductInventory inventory = new ProductInventory();

        if (inventoryPO == null) {
            inventory.setId(id);
            inventory.setProductId(id);
            inventory.setQuantity(0);
            inventory.setCreatedBy(contextAccessor.getMemberId());
            inventory.setCreatedAt(LocalDateTime.now());
            inventory.setUpdatedBy(contextAccessor.getMemberId());
            inventory.setUpdatedAt(LocalDateTime.now());
            inventory.setVersion(DomainConstants.EntityInitialVersion);
        } else {
            inventory.setId(inventoryPO.getId());
            inventory.setProductId(inventoryPO.getProductId());
            inventory.setQuantity(inventoryPO.getQuantity());
            inventory.setCreatedBy(String.valueOf(inventoryPO.getCreatedBy()));
            inventory.setCreatedAt(inventoryPO.getCreatedAt());
            inventory.setUpdatedBy(String.valueOf(inventoryPO.getUpdatedBy()));
            inventory.setUpdatedAt(inventoryPO.getUpdatedAt());
            inventory.setVersion(inventoryPO.getVersion());
        }
        return inventory;
    }

    @Override
    public void save(ProductInventory inventory) {
        if (inventory.getVersion().equals(1)) {
            ProductInventoryPO inventoryPO = new ProductInventoryPO();
            inventoryPO.setId(inventory.getId());
            inventoryPO.setAccount(inventory.getAccount());
            inventoryPO.setProductId(inventory.getProductId());
            inventoryPO.setQuantity(inventory.getQuantity());
            inventoryPO.setCreatedBy(Long.valueOf(inventory.getCreatedBy()));
            inventoryPO.setCreatedAt(inventory.getCreatedAt());
            inventoryPO.setUpdatedBy(Long.valueOf(inventory.getUpdatedBy()));
            inventoryPO.setUpdatedAt(inventory.getUpdatedAt());
            inventoryPO.setVersion(inventory.getVersion() + 1);
            productInventoryMapper.insert(inventoryPO);
        } else {
            update(inventory);
        }
    }

    @Override
    public void update(ProductInventory inventory) {
        ProductInventoryPO inventoryPO = new ProductInventoryPO();
        inventoryPO.setId(inventory.getId());
        inventoryPO.setAccount(inventory.getAccount());
        inventoryPO.setProductId(inventory.getProductId());
        inventoryPO.setQuantity(inventory.getQuantity());
        inventoryPO.setUpdatedBy(Long.valueOf(inventory.getUpdatedBy()));
        inventoryPO.setUpdatedAt(inventory.getUpdatedAt());
        inventoryPO.setVersion(inventory.getVersion());

        int rowsAffected = productInventoryMapper.updateByIdAndVersion(inventoryPO);
        if (rowsAffected == 0) {
            throw new OptimisticLockException();
        }
    }
}
