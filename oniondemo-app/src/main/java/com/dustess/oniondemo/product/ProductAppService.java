package com.dustess.oniondemo.product;

import com.dustess.oniondemo.EventPublisher;
import com.dustess.oniondemo.ProcessCallback;
import com.dustess.oniondemo.builder.ProductVOBuilder;
import com.dustess.oniondemo.dto.CreateProductDTO;
import com.dustess.oniondemo.entity.CreatedResult;
import com.dustess.oniondemo.entity.product.Product;
import com.dustess.oniondemo.entity.product.ProductFactory;
import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import com.dustess.oniondemo.exception.OptimisticLockException;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.interfaces.TimeService;
import com.dustess.oniondemo.repository.ProductRepository;
import com.dustess.oniondemo.vo.ProductDetailVO;
import com.dustess.oniondemo.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductAppService {

    @Autowired
    private ProductFactory productFactory;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TimeService timeService;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private ContextAccessor contextAccessor;

    @Autowired
    private ProductVOBuilder productVOBuilder;

    @Transactional(rollbackFor = Exception.class)
    public ProductVO create(CreateProductDTO dto) {
        if (productRepository.isNameDuplicated(dto.getName())) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }

        CreatedResult<Product> result = productFactory.create(dto);
        Product product = result.getEntity();
        productRepository.save(product);
        eventPublisher.publish(result.getEvent());

        return productVOBuilder.buildProductVO(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductVO publish(Long productId) {
        String memberId = contextAccessor.getMemberId();
        return loadThenDo(productId, p -> p.publish(memberId, timeService.now()));
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductVO archive(Long productId) {
        String memberId = contextAccessor.getMemberId();
        return loadThenDo(productId, p -> p.archive(memberId, timeService.now()));
    }

    private ProductVO loadThenDo(Long productId, ProcessCallback<Product> callback) {
        Product product = productRepository.load(productId);
        if (product == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        DomainEvent event = callback.onProcess(product);
        try {
            productRepository.update(product);
        } catch (OptimisticLockException e) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        eventPublisher.publish(event);

        return productVOBuilder.buildProductVO(product);
    }

    public ProductDetailVO get(Long id) {
        Product product = productRepository.load(id);
        if (product == null) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        return productVOBuilder.buildProductDetailVO(product);
    }
}
