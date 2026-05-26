package com.returnradar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationReturnResponse {

    private String city;
    private Integer totalReturns;
    private Double totalRefundAmount;
}
