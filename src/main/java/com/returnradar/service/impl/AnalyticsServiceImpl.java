package com.returnradar.service.impl;

import com.returnradar.dto.response.AnalyticsSnapshotResponse;
import com.returnradar.dto.response.DataQualityIssueResponse;
import com.returnradar.dto.response.HighReturnProductResponse;
import com.returnradar.dto.response.LocationReturnResponse;
import com.returnradar.dto.response.ReturnReasonSummary;
import com.returnradar.dto.response.SuspiciousCustomerResponse;
import com.returnradar.entity.AnalyticsSnapshot;
import com.returnradar.entity.Customer;
import com.returnradar.entity.DataQualityIssue;
import com.returnradar.entity.Product;
import com.returnradar.entity.ReturnRequest;
import com.returnradar.enums.ReturnReason;
import com.returnradar.exception.ResourceNotFoundException;
import com.returnradar.repository.AnalyticsSnapshotRepository;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.repository.DataQualityIssueRepository;
import com.returnradar.repository.ProductRepository;
import com.returnradar.repository.ReturnRequestRepository;
import com.returnradar.service.AnalyticsService;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final double HIGH_RETURN_RATE_THRESHOLD = 10.0;

    private final ReturnRequestRepository returnRequestRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final DataQualityIssueRepository dataQualityIssueRepository;
    private final AnalyticsSnapshotRepository analyticsSnapshotRepository;

    @Override
    public List<HighReturnProductResponse> getHighReturnProducts() {
        return productRepository.findByReturnRateGreaterThan(HIGH_RETURN_RATE_THRESHOLD)
                .stream()
                .sorted(Comparator.comparing(
                        Product::getReturnRate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toHighReturnProductResponse)
                .toList();
    }

    @Override
    public List<ReturnReasonSummary> getReturnReasonSummary() {
        List<ReturnRequest> returns = returnRequestRepository.findAll();
        long totalReturns = returns.size();

        if (totalReturns == 0) {
            return List.of();
        }

        Map<ReturnReason, Long> countsByReason = returns.stream()
                .filter(returnRequest -> returnRequest.getReturnReason() != null)
                .collect(Collectors.groupingBy(ReturnRequest::getReturnReason, Collectors.counting()));

        return countsByReason.entrySet()
                .stream()
                .map(entry -> ReturnReasonSummary.builder()
                        .reason(entry.getKey())
                        .count(entry.getValue())
                        .percentage((entry.getValue() * 100.0) / totalReturns)
                        .build())
                .sorted(Comparator.comparing(ReturnReasonSummary::getCount).reversed())
                .toList();
    }

    @Override
    public List<HighReturnProductResponse> getSizeIssueProducts() {
        Set<Long> productIds = returnRequestRepository.findAll()
                .stream()
                .filter(this::isSizeIssueReturn)
                .map(ReturnRequest::getProductId)
                .collect(Collectors.toSet());

        if (productIds.isEmpty()) {
            return List.of();
        }

        return productRepository.findAllById(productIds)
                .stream()
                .map(this::toHighReturnProductResponse)
                .toList();
    }

    @Override
    public List<LocationReturnResponse> getLocationWiseReturns() {
        return returnRequestRepository.findAll()
                .stream()
                .filter(returnRequest -> returnRequest.getCity() != null)
                .collect(Collectors.groupingBy(ReturnRequest::getCity))
                .entrySet()
                .stream()
                .map(entry -> LocationReturnResponse.builder()
                        .city(entry.getKey())
                        .totalReturns(entry.getValue().size())
                        .totalRefundAmount(entry.getValue()
                                .stream()
                                .map(ReturnRequest::getRefundAmount)
                                .mapToDouble(this::valueOrZero)
                                .sum())
                        .build())
                .sorted(Comparator.comparing(LocationReturnResponse::getTotalReturns).reversed())
                .toList();
    }

    @Override
    public List<SuspiciousCustomerResponse> getSuspiciousCustomers() {
        return customerRepository.findByFlaggedAsSuspiciousTrue()
                .stream()
                .map(this::toSuspiciousCustomerResponse)
                .toList();
    }

    @Override
    public List<DataQualityIssueResponse> getDataQualityIssues() {
        return dataQualityIssueRepository.findByResolvedFalse()
                .stream()
                .map(this::toDataQualityIssueResponse)
                .toList();
    }

    @Override
    public AnalyticsSnapshotResponse getLatestSnapshot() {
        return analyticsSnapshotRepository.findTopByOrderBySnapshotTimeDesc()
                .map(this::toAnalyticsSnapshotResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Analytics snapshot not found"));
    }

    private boolean isSizeIssueReturn(ReturnRequest returnRequest) {
        return returnRequest.getReturnReason() == ReturnReason.SIZE_TOO_SMALL
                || returnRequest.getReturnReason() == ReturnReason.SIZE_TOO_LARGE;
    }

    private HighReturnProductResponse toHighReturnProductResponse(Product product) {
        return HighReturnProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .brand(product.getBrand())
                .category(product.getCategory())
                .totalReturned(product.getTotalReturned())
                .totalSold(product.getTotalSold())
                .returnRate(product.getReturnRate())
                .build();
    }

    private SuspiciousCustomerResponse toSuspiciousCustomerResponse(Customer customer) {
        return SuspiciousCustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .city(customer.getCity())
                .totalReturns(customer.getTotalReturns())
                .totalRefundAmount(customer.getTotalRefundAmount())
                .flaggedAsSuspicious(customer.getFlaggedAsSuspicious())
                .build();
    }

    private DataQualityIssueResponse toDataQualityIssueResponse(DataQualityIssue issue) {
        return DataQualityIssueResponse.builder()
                .issueId(issue.getIssueId())
                .issueType(issue.getIssueType())
                .referenceId(issue.getReferenceId())
                .referenceType(issue.getReferenceType())
                .description(issue.getDescription())
                .detectedAt(issue.getDetectedAt())
                .resolved(issue.getResolved())
                .build();
    }

    private AnalyticsSnapshotResponse toAnalyticsSnapshotResponse(AnalyticsSnapshot snapshot) {
        return AnalyticsSnapshotResponse.builder()
                .snapshotId(snapshot.getSnapshotId())
                .snapshotTime(snapshot.getSnapshotTime())
                .totalReturns(snapshot.getTotalReturns())
                .totalRefundAmount(snapshot.getTotalRefundAmount())
                .highReturnProductCount(snapshot.getHighReturnProductCount())
                .suspiciousCustomerCount(snapshot.getSuspiciousCustomerCount())
                .mostCommonReason(snapshot.getMostCommonReason())
                .averageRefundAmount(snapshot.getAverageRefundAmount())
                .dailyReturnVolume(snapshot.getDailyReturnVolume())
                .build();
    }

    private double valueOrZero(Double value) {
        return value == null ? 0.0 : value;
    }
}
