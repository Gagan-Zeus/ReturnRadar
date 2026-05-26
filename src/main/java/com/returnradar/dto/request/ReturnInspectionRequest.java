package com.returnradar.dto.request;

import com.returnradar.enums.ProductCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnInspectionRequest {

    @NotNull
    private Long returnId;

    @NotBlank
    private String warehouseId;

    @NotNull
    private ProductCondition productCondition;

    @NotNull
    private Boolean approved;

    private String rejectionReason;

    @NotBlank
    private String inspectorName;
}
