package com.returnradar.dto.response;

import com.returnradar.enums.ProductCondition;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnInspectionResponse {

    private Long inspectionId;
    private Long returnId;
    private String warehouseId;
    private ProductCondition productCondition;
    private Boolean approved;
    private String rejectionReason;
    private LocalDateTime inspectedAt;
    private String inspectorName;
}
