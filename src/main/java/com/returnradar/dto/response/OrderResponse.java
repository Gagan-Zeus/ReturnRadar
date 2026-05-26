package com.returnradar.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private Double totalAmount;
    private String city;
    private String status;
    private List<OrderItemResponse> items;
}
