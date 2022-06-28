package com.dustess.oniondemo.entity.product;

import com.dustess.oniondemo.event.DomainEvent;
import com.dustess.oniondemo.event.product.ProductArchived;
import com.dustess.oniondemo.event.product.ProductPublished;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String account;
    private String name;
    private Integer price;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Integer version;

    public DomainEvent publish(String updatedBy, LocalDateTime updatedAt) {
        if (this.status == ProductStatus.Published) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        this.status = ProductStatus.Published;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        return new ProductPublished(this.id, this.account, this.updatedBy, this.updatedAt);
    }

    public DomainEvent archive(String updatedBy, LocalDateTime updatedAt) {
        if (this.status == ProductStatus.UnPublished) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        this.status = ProductStatus.UnPublished;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        return new ProductArchived(this.id, this.account, this.updatedBy, this.updatedAt);
    }
}
