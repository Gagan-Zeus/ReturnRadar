package com.returnradar.service;

import com.returnradar.dto.request.ProductRequest;
import com.returnradar.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();
}
