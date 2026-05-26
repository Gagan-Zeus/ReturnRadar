package com.returnradar.mapper;

import com.returnradar.dto.request.ReturnInspectionRequest;
import com.returnradar.dto.response.ReturnInspectionResponse;
import com.returnradar.entity.ReturnInspection;
import com.returnradar.entity.ReturnRequest;

public final class InspectionMapper {

    private InspectionMapper() {
    }

    public static ReturnInspectionResponse toResponse(ReturnInspection inspection) {
        if (inspection == null) {
            return null;
        }

        Long returnId = inspection.getReturnRequest() == null
                ? null
                : inspection.getReturnRequest().getReturnId();

        return ReturnInspectionResponse.builder()
                .inspectionId(inspection.getInspectionId())
                .returnId(returnId)
                .warehouseId(inspection.getWarehouseId())
                .productCondition(inspection.getProductCondition())
                .approved(inspection.getApproved())
                .rejectionReason(inspection.getRejectionReason())
                .inspectedAt(inspection.getInspectedAt())
                .inspectorName(inspection.getInspectorName())
                .build();
    }

    public static ReturnInspection toEntity(ReturnInspectionRequest request, ReturnRequest returnRequest) {
        if (request == null) {
            return null;
        }

        return ReturnInspection.builder()
                .returnRequest(returnRequest)
                .warehouseId(request.getWarehouseId())
                .productCondition(request.getProductCondition())
                .approved(request.getApproved())
                .rejectionReason(request.getRejectionReason())
                .inspectorName(request.getInspectorName())
                .build();
    }
}
