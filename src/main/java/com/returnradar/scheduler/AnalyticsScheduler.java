package com.returnradar.scheduler;

import com.returnradar.entity.AnalyticsSnapshot;
import com.returnradar.entity.ReturnRequest;
import com.returnradar.repository.AnalyticsSnapshotRepository;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.repository.ProductRepository;
import com.returnradar.repository.ReturnRequestRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsScheduler {

    private static final double HIGH_RETURN_RATE_THRESHOLD = 10.0;

    private final ReturnRequestRepository returnRequestRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AnalyticsSnapshotRepository analyticsSnapshotRepository;

    @Scheduled(fixedRate = 3600000)
    public void aggregateAnalytics() {
        List<ReturnRequest> returns = returnRequestRepository.findAll();
        int totalReturns = returns.size();
        double totalRefundAmount = returns.stream()
                .map(ReturnRequest::getRefundAmount)
                .mapToDouble(this::valueOrZero)
                .sum();
        int highReturnProductCount = productRepository.findByReturnRateGreaterThan(HIGH_RETURN_RATE_THRESHOLD).size();
        int suspiciousCustomerCount = customerRepository.findByFlaggedAsSuspiciousTrue().size();
        String mostCommonReason = findMostCommonReason(returns);
        double averageRefundAmount = totalReturns == 0 ? 0.0 : totalRefundAmount / totalReturns;
        int dailyReturnVolume = countReturnsCreatedToday(returns);

        LocalDateTime snapshotTime = LocalDateTime.now();
        AnalyticsSnapshot snapshot = AnalyticsSnapshot.builder()
                .snapshotTime(snapshotTime)
                .totalReturns(totalReturns)
                .totalRefundAmount(totalRefundAmount)
                .highReturnProductCount(highReturnProductCount)
                .suspiciousCustomerCount(suspiciousCustomerCount)
                .mostCommonReason(mostCommonReason)
                .averageRefundAmount(averageRefundAmount)
                .dailyReturnVolume(dailyReturnVolume)
                .build();

        analyticsSnapshotRepository.save(snapshot);
        log.info("Analytics snapshot saved at {}", snapshotTime);
    }

    private String findMostCommonReason(List<ReturnRequest> returns) {
        return returns.stream()
                .filter(returnRequest -> returnRequest.getReturnReason() != null)
                .collect(Collectors.groupingBy(ReturnRequest::getReturnReason, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().name())
                .orElse(null);
    }

    private int countReturnsCreatedToday(List<ReturnRequest> returns) {
        LocalDate today = LocalDate.now();

        return (int) returns.stream()
                .map(ReturnRequest::getRequestedAt)
                .filter(requestedAt -> requestedAt != null && requestedAt.toLocalDate().equals(today))
                .count();
    }

    private double valueOrZero(Double value) {
        return value == null ? 0.0 : value;
    }
}
