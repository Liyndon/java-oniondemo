package com.dustess.oniondemo.entity.product;

import com.dustess.oniondemo.constants.DomainConstants;
import com.dustess.oniondemo.dto.CreateProductDTO;
import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.event.product.ProductCreated;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.interfaces.IDGenerator;
import com.dustess.oniondemo.interfaces.TimeService;
import com.dustess.oniondemo.entity.CreatedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductFactory {

    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private ContextAccessor contextAccessor;

    @Autowired
    private TimeService timeService;

    public CreatedResult<Product> create(CreateProductDTO dto) {
        Long productId = idGenerator.nextId();
        String userId = contextAccessor.getMemberId();
        String account = contextAccessor.getAccount();
        LocalDateTime createdAt = timeService.now();

        Product product = new Product();
        product.setId(productId);
        product.setAccount(account);
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setStatus(ProductStatus.UnPublished);
        product.setCreatedBy(userId);
        product.setCreatedAt(createdAt);
        product.setUpdatedBy(userId);
        product.setUpdatedAt(createdAt);
        product.setVersion(DomainConstants.EntityInitialVersion);

        DomainEvent event = new ProductCreated(
                product.getId(),
                product.getAccount(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getCreatedAt()
        );

        return new CreatedResult<>(product, event);
    }
}
