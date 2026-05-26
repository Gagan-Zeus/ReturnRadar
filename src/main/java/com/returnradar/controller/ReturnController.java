package com.returnradar.controller;

import com.returnradar.dto.request.ReturnRequestDto;
import com.returnradar.dto.response.ApiResponse;
import com.returnradar.dto.response.ReturnRequestResponse;
import com.returnradar.enums.ReturnStatus;
import com.returnradar.service.ReturnRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
@Tag(name = "Returns")
public class ReturnController {

    private final ReturnRequestService returnRequestService;

    @PostMapping
    @Operation(
            summary = "Create return request",
            description = "Creates a new return request after validating order, product, customer, and business rules."
    )
    public ResponseEntity<ApiResponse<ReturnRequestResponse>> createReturn(
            @Valid @RequestBody ReturnRequestDto request
    ) {
        ReturnRequestResponse response = returnRequestService.createReturn(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ReturnRequestResponse>builder()
                        .success(true)
                        .message("Return request created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    @Operation(
            summary = "Get all returns",
            description = "Fetches all return requests currently stored in ReturnRadar."
    )
    public ResponseEntity<ApiResponse<List<ReturnRequestResponse>>> getAllReturns() {
        List<ReturnRequestResponse> response = returnRequestService.getAllReturns();
        return ResponseEntity.ok(ApiResponse.<List<ReturnRequestResponse>>builder()
                .success(true)
                .message("Return requests fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get return by ID",
            description = "Fetches a single return request by its return ID."
    )
    public ResponseEntity<ApiResponse<ReturnRequestResponse>> getReturnById(@PathVariable Long id) {
        ReturnRequestResponse response = returnRequestService.getReturnById(id);
        return ResponseEntity.ok(ApiResponse.<ReturnRequestResponse>builder()
                .success(true)
                .message("Return request fetched successfully")
                .data(response)
                .build());
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update return status",
            description = "Updates the lifecycle status of an existing return request."
    )
    public ResponseEntity<ApiResponse<ReturnRequestResponse>> updateReturnStatus(
            @PathVariable Long id,
            @RequestParam ReturnStatus status
    ) {
        ReturnRequestResponse response = returnRequestService.updateReturnStatus(id, status);
        return ResponseEntity.ok(ApiResponse.<ReturnRequestResponse>builder()
                .success(true)
                .message("Return status updated successfully")
                .data(response)
                .build());
    }
}
