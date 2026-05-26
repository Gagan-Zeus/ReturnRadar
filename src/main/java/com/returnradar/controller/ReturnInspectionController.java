package com.returnradar.controller;

import com.returnradar.dto.request.ReturnInspectionRequest;
import com.returnradar.dto.response.ApiResponse;
import com.returnradar.dto.response.ReturnInspectionResponse;
import com.returnradar.service.ReturnInspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inspections")
@RequiredArgsConstructor
@Tag(name = "Return Inspections")
public class ReturnInspectionController {

    private final ReturnInspectionService returnInspectionService;

    @PostMapping
    @Operation(
            summary = "Create return inspection",
            description = "Creates a warehouse inspection record for an existing return request."
    )
    public ResponseEntity<ApiResponse<ReturnInspectionResponse>> createInspection(
            @Valid @RequestBody ReturnInspectionRequest request
    ) {
        ReturnInspectionResponse response = returnInspectionService.createInspection(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ReturnInspectionResponse>builder()
                        .success(true)
                        .message("Return inspection created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/return/{returnId}")
    @Operation(
            summary = "Get inspection by return ID",
            description = "Fetches the inspection record associated with a return request ID."
    )
    public ResponseEntity<ApiResponse<ReturnInspectionResponse>> getInspectionByReturnId(
            @PathVariable Long returnId
    ) {
        ReturnInspectionResponse response = returnInspectionService.getInspectionByReturnId(returnId);
        return ResponseEntity.ok(ApiResponse.<ReturnInspectionResponse>builder()
                .success(true)
                .message("Return inspection fetched successfully")
                .data(response)
                .build());
    }
}
