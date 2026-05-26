package com.returnradar.mapper;

import com.returnradar.dto.request.ReturnRequestDto;
import com.returnradar.dto.response.ReturnRequestResponse;
import com.returnradar.entity.ReturnRequest;

public final class ReturnMapper {

    private ReturnMapper() {
    }

    public static ReturnRequestResponse toResponse(ReturnRequest returnRequest) {
        if (returnRequest == null) {
            return null;
        }

        return ReturnRequestResponse.builder()
                .returnId(returnRequest.getReturnId())
                .orderId(returnRequest.getOrderId())
                .productId(returnRequest.getProductId())
                .customerId(returnRequest.getCustomerId())
                .reason(returnRequest.getReturnReason())
                .description(returnRequest.getDescription())
                .quantity(returnRequest.getQuantity())
                .returnStatus(returnRequest.getReturnStatus())
                .requestedAt(returnRequest.getRequestedAt())
                .pickupDate(returnRequest.getPickupDate())
                .city(returnRequest.getCity())
                .refundAmount(returnRequest.getRefundAmount())
                .build();
    }

    public static ReturnRequest toEntity(ReturnRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return ReturnRequest.builder()
                .orderId(dto.getOrderId())
                .productId(dto.getProductId())
                .customerId(dto.getCustomerId())
                .returnReason(dto.getReason())
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .pickupDate(dto.getPickupDate())
                .city(dto.getCity())
                .refundAmount(dto.getRefundAmount())
                .build();
    }
}
