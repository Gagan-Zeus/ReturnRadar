package com.returnradar.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class OrderRequest {

    @NotNull
    private Long customerId;

    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    private Double totalAmount;

    private String city;

    @NotEmpty
    private List<OrderItemRequest> items;
}
