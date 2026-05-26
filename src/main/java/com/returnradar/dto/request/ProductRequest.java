package com.returnradar.dto.request;

import com.returnradar.enums.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank
    private String name;

    private String brand;

    private ProductCategory category;

    private String size;

    private String color;

    @Positive
    private Double price;

    @Min(0)
    private Integer stockQuantity;
}
