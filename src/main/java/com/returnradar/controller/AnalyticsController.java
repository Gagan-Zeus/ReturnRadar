package com.returnradar.controller;

import com.returnradar.dto.response.AnalyticsSnapshotResponse;
import com.returnradar.dto.response.ApiResponse;
import com.returnradar.dto.response.DataQualityIssueResponse;
import com.returnradar.dto.response.HighReturnProductResponse;
import com.returnradar.dto.response.LocationReturnResponse;
import com.returnradar.dto.response.ReturnReasonSummary;
import com.returnradar.dto.response.SuspiciousCustomerResponse;
import com.returnradar.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Return analytics and insights")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/high-return-products")
    @Operation(summary = "Get high-return products")
    public ResponseEntity<ApiResponse<List<HighReturnProductResponse>>> getHighReturnProducts() {
        List<HighReturnProductResponse> response = analyticsService.getHighReturnProducts();
        return ResponseEntity.ok(success("High-return products fetched successfully", response));
    }

    @GetMapping("/return-reasons")
    @Operation(summary = "Get return reason summary")
    public ResponseEntity<ApiResponse<List<ReturnReasonSummary>>> getReturnReasonSummary() {
        List<ReturnReasonSummary> response = analyticsService.getReturnReasonSummary();
        return ResponseEntity.ok(success("Return reason summary fetched successfully", response));
    }

    @GetMapping("/size-issue-products")
    @Operation(summary = "Get products with size-related return issues")
    public ResponseEntity<ApiResponse<List<HighReturnProductResponse>>> getSizeIssueProducts() {
        List<HighReturnProductResponse> response = analyticsService.getSizeIssueProducts();
        return ResponseEntity.ok(success("Size issue products fetched successfully", response));
    }

    @GetMapping("/location-wise-returns")
    @Operation(summary = "Get location-wise return summary")
    public ResponseEntity<ApiResponse<List<LocationReturnResponse>>> getLocationWiseReturns() {
        List<LocationReturnResponse> response = analyticsService.getLocationWiseReturns();
        return ResponseEntity.ok(success("Location-wise returns fetched successfully", response));
    }

    @GetMapping("/suspicious-customers")
    @Operation(summary = "Get suspicious customers")
    public ResponseEntity<ApiResponse<List<SuspiciousCustomerResponse>>> getSuspiciousCustomers() {
        List<SuspiciousCustomerResponse> response = analyticsService.getSuspiciousCustomers();
        return ResponseEntity.ok(success("Suspicious customers fetched successfully", response));
    }

    @GetMapping("/data-quality-issues")
    @Operation(summary = "Get unresolved data quality issues")
    public ResponseEntity<ApiResponse<List<DataQualityIssueResponse>>> getDataQualityIssues() {
        List<DataQualityIssueResponse> response = analyticsService.getDataQualityIssues();
        return ResponseEntity.ok(success("Data quality issues fetched successfully", response));
    }

    @GetMapping("/snapshot")
    @Operation(summary = "Get latest analytics snapshot")
    public ResponseEntity<ApiResponse<AnalyticsSnapshotResponse>> getLatestSnapshot() {
        AnalyticsSnapshotResponse response = analyticsService.getLatestSnapshot();
        return ResponseEntity.ok(success("Latest analytics snapshot fetched successfully", response));
    }

    private <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}
