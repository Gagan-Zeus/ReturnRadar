package com.returnradar.dto.response;

import com.returnradar.enums.ReturnReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnReasonSummary {

    private ReturnReason reason;
    private Long count;
    private Double percentage;
}
