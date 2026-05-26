package com.returnradar.repository;

import com.returnradar.entity.Product;
import com.returnradar.enums.ProductCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByReturnRateGreaterThan(Double rate);

    List<Product> findByCategoryOrderByReturnRateDesc(ProductCategory category);
}
