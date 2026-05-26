package com.returnradar.dto.request;

import com.returnradar.enums.ReturnReason;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequestDto {

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @NotNull
    private Long customerId;

    @NotNull
    private ReturnReason reason;

    private String description;

    @Min(1)
    private Integer quantity;

    private LocalDateTime pickupDate;

    private String city;

    @Min(0)
    private Double refundAmount;
}
