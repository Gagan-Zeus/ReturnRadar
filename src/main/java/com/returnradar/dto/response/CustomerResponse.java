package com.returnradar.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String city;
    private LocalDateTime registeredAt;
    private Integer totalOrders;
    private Integer totalReturns;
    private Double totalRefundAmount;
    private Boolean flaggedAsSuspicious;
}
