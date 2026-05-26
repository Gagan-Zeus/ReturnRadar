package com.returnradar.service.impl;

import com.returnradar.dto.request.ProductRequest;
import com.returnradar.dto.response.ProductResponse;
import com.returnradar.entity.Product;
import com.returnradar.exception.ResourceNotFoundException;
import com.returnradar.mapper.ProductMapper;
import com.returnradar.repository.ProductRepository;
import com.returnradar.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = ProductMapper.toEntity(request);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }
}
