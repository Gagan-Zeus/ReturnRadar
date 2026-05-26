package com.returnradar.dto.response;

import com.returnradar.enums.ProductCategory;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long productId;
    private String name;
    private String brand;
    private ProductCategory category;
    private String size;
    private String color;
    private Double price;
    private Integer stockQuantity;
    private Integer totalSold;
    private Integer totalReturned;
    private Double returnRate;
    private LocalDateTime createdAt;
}
