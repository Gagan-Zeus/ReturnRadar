package com.returnradar.entity;

import com.returnradar.enums.ProductCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String size;

    private String color;

    private Double price;

    private Integer stockQuantity;

    @Builder.Default
    private Integer totalSold = 0;

    @Builder.Default
    private Integer totalReturned = 0;

    @Builder.Default
    private Double returnRate = 0.0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
