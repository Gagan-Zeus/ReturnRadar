package com.returnradar.service;

import com.returnradar.dto.response.AnalyticsSnapshotResponse;
import com.returnradar.dto.response.DataQualityIssueResponse;
import com.returnradar.dto.response.HighReturnProductResponse;
import com.returnradar.dto.response.LocationReturnResponse;
import com.returnradar.dto.response.ReturnReasonSummary;
import com.returnradar.dto.response.SuspiciousCustomerResponse;
import java.util.List;

public interface AnalyticsService {

    List<HighReturnProductResponse> getHighReturnProducts();

    List<ReturnReasonSummary> getReturnReasonSummary();

    List<HighReturnProductResponse> getSizeIssueProducts();

    List<LocationReturnResponse> getLocationWiseReturns();

    List<SuspiciousCustomerResponse> getSuspiciousCustomers();

    List<DataQualityIssueResponse> getDataQualityIssues();

    AnalyticsSnapshotResponse getLatestSnapshot();
}
