package com.dustess.oniondemo.entity.product;

import lombok.Getter;

@Getter
public enum ProductStatus {
    UnPublished(0, "未上架"),
    Published(1, "已上架");

    private Integer code;
    private String value;

    ProductStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ProductStatus get(Integer type) {
        ProductStatus[] enums = values();
        for (ProductStatus obj : enums) {
            if (obj.getCode().equals(type)) {
                return obj;
            }
        }
        return null;
    }
}