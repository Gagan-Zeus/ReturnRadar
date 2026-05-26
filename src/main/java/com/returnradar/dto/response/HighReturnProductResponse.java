package com.returnradar.dto.response;

import com.returnradar.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighReturnProductResponse {

    private Long productId;
    private String name;
    private String brand;
    private ProductCategory category;
    private Integer totalReturned;
    private Integer totalSold;
    private Double returnRate;
}
