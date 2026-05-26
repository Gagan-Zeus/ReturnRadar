package com.returnradar.entity;

import com.returnradar.enums.IssueType;
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
@Table(name = "data_quality_issues")
public class DataQualityIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @Enumerated(EnumType.STRING)
    private IssueType issueType;

    private Long referenceId;

    private String referenceType;

    private String description;

    @Builder.Default
    private LocalDateTime detectedAt = LocalDateTime.now();

    @Builder.Default
    private Boolean resolved = false;
}
