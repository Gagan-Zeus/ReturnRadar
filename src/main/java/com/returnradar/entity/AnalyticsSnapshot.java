package com.returnradar.entity;

import jakarta.persistence.Entity;
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
@Table(name = "analytics_snapshots")
public class AnalyticsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
