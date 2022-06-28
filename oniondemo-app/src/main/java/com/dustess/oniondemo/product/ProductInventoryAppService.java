package com.dustess.oniondemo.product;

import com.dustess.oniondemo.EventPublisher;
import com.dustess.oniondemo.entity.product.Product;
import com.dustess.oniondemo.entity.product.ProductInventory;
import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import com.dustess.oniondemo.exception.OptimisticLockException;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.interfaces.TimeService;
import com.dustess.oniondemo.repository.ProductInventoryRepository;
import com.dustess.oniondemo.repository.ProductRepository;
import com.dustess.oniondemo.vo.ProductInventoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductInventoryAppService {

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TimeService timeService;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private ContextAccessor contextAccessor;

    @Transactional(rollbackFor = Exception.class)
    public ProductInventoryVO increase(Long productId, int quantity) {
        Product product = productRepository.load(productId);
        if (product == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }

        String memberId = contextAccessor.getMemberId();
        ProductInventory inventory = productInventoryRepository.load(productId);
        DomainEvent event = inventory.increase(quantity, memberId, timeService.now());
        try {
            productInventoryRepository.save(inventory);
        } catch (OptimisticLockException e) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        eventPublisher.publish(event);

        return ProductInventoryVO.builder().id(String.valueOf(product.getId()))
                .name(product.getName())
                .quantity(inventory.getQuantity())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductInventoryVO decrease(Long productId, int quantity) {
        Product product = productRepository.load(productId);
        if (product == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }

        String memberId = contextAccessor.getMemberId();
        ProductInventory inventory = productInventoryRepository.load(productId);
        DomainEvent event = inventory.decrease(quantity, memberId, timeService.now());
        try {
            productInventoryRepository.save(inventory);
        } catch (OptimisticLockException e) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        eventPublisher.publish(event);

        return ProductInventoryVO.builder().id(String.valueOf(product.getId()))
                .name(product.getName())
                .quantity(inventory.getQuantity())
                .build();
    }
}
