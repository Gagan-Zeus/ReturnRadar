package com.returnradar.dto.response;

import com.returnradar.enums.ReturnReason;
import com.returnradar.enums.ReturnStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequestResponse {

    private Long returnId;
    private Long orderId;
    private Long productId;
    private Long customerId;
    private ReturnReason reason;
    private String description;
    private Integer quantity;
    private ReturnStatus returnStatus;
    private LocalDateTime requestedAt;
    private LocalDateTime pickupDate;
    private String city;
    private Double refundAmount;
}
