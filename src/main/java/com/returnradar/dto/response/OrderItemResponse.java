package com.returnradar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long orderItemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private String size;
    private Boolean returnRequested;
}
