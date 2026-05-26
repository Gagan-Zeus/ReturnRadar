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
public class AnalyticsSnapshotResponse {

    private Long snapshotId;
    private LocalDateTime snapshotTime;
    private Integer totalReturns;
    private Double totalRefundAmount;
    private Integer highReturnProductCount;
    private Integer suspiciousCustomerCount;
    private String mostCommonReason;
    private Double averageRefundAmount;
    private Integer dailyReturnVolume;
}
