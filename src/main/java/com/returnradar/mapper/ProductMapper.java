package com.returnradar.mapper;

import com.returnradar.dto.request.ProductRequest;
import com.returnradar.dto.response.ProductResponse;
import com.returnradar.entity.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .brand(product.getBrand())
                .category(product.getCategory())
                .size(product.getSize())
                .color(product.getColor())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .totalSold(product.getTotalSold())
                .totalReturned(product.getTotalReturned())
                .returnRate(product.getReturnRate())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public static Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        return Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .category(request.getCategory())
                .size(request.getSize())
                .color(request.getColor())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();
    }
}
