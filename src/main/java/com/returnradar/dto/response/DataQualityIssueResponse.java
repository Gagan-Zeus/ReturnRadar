package com.returnradar.dto.response;

import com.returnradar.enums.IssueType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataQualityIssueResponse {

    private Long issueId;
    private IssueType issueType;
    private Long referenceId;
    private String referenceType;
    private String description;
    private LocalDateTime detectedAt;
    private Boolean resolved;
}
