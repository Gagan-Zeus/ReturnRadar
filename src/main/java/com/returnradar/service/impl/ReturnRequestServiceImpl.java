package com.returnradar.service.impl;

import com.returnradar.dto.request.ReturnRequestDto;
import com.returnradar.dto.response.ReturnRequestResponse;
import com.returnradar.entity.Customer;
import com.returnradar.entity.DataQualityIssue;
import com.returnradar.entity.Order;
import com.returnradar.entity.Product;
import com.returnradar.entity.ReturnRequest;
import com.returnradar.enums.IssueType;
import com.returnradar.enums.ReturnStatus;
import com.returnradar.exception.BusinessRuleException;
import com.returnradar.exception.DuplicateReturnException;
import com.returnradar.exception.ResourceNotFoundException;
import com.returnradar.exception.ValidationException;
import com.returnradar.mapper.ReturnMapper;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.repository.DataQualityIssueRepository;
import com.returnradar.repository.OrderRepository;
import com.returnradar.repository.ProductRepository;
import com.returnradar.repository.ReturnRequestRepository;
import com.returnradar.service.ReturnRequestService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReturnRequestServiceImpl implements ReturnRequestService {

    private static final int SUSPICIOUS_RETURN_COUNT_THRESHOLD = 5;
    private static final double SUSPICIOUS_REFUND_AMOUNT_THRESHOLD = 5000.0;
    private static final double REFUND_TOLERANCE_MULTIPLIER = 1.2;

    private final ReturnRequestRepository returnRequestRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DataQualityIssueRepository dataQualityIssueRepository;

    @Override
    @Transactional
    public ReturnRequestResponse createReturn(ReturnRequestDto dto) {
        validateReturnValues(dto);

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + dto.getCustomerId()));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + dto.getProductId()));
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + dto.getOrderId()));

        if (returnRequestRepository.existsByOrderIdAndProductId(dto.getOrderId(), dto.getProductId())) {
            throw new DuplicateReturnException(
                    "Return already exists for order id " + dto.getOrderId()
                            + " and product id " + dto.getProductId());
        }

        LocalDateTime requestedAt = LocalDateTime.now();
        if (order.getOrderDate() != null && !requestedAt.isAfter(order.getOrderDate())) {
            throw new BusinessRuleException("Return request date must be after order date");
        }

        logInvalidRefundAmountIfNeeded(dto, product);

        ReturnRequest returnRequest = ReturnMapper.toEntity(dto);
        returnRequest.setRequestedAt(requestedAt);
        ReturnRequest savedReturnRequest = returnRequestRepository.save(returnRequest);

        updateCustomerReturnStats(customer, dto.getRefundAmount());
        updateProductReturnStats(product, dto.getQuantity());

        return ReturnMapper.toResponse(savedReturnRequest);
    }

    @Override
    public ReturnRequestResponse getReturnById(Long id) {
        return returnRequestRepository.findById(id)
                .map(ReturnMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Return request not found with id: " + id));
    }

    @Override
    public List<ReturnRequestResponse> getAllReturns() {
        return returnRequestRepository.findAll()
                .stream()
                .map(ReturnMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReturnRequestResponse updateReturnStatus(Long id, ReturnStatus newStatus) {
        ReturnRequest returnRequest = returnRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Return request not found with id: " + id));

        returnRequest.setReturnStatus(newStatus);
        return ReturnMapper.toResponse(returnRequestRepository.save(returnRequest));
    }

    private void validateReturnValues(ReturnRequestDto dto) {
        if (dto.getQuantity() == null || dto.getQuantity() < 1) {
            throw new ValidationException("Return quantity must be at least 1");
        }

        if (dto.getRefundAmount() != null && dto.getRefundAmount() < 0) {
            throw new ValidationException("Refund amount cannot be negative");
        }
    }

    private void logInvalidRefundAmountIfNeeded(ReturnRequestDto dto, Product product) {
        if (dto.getRefundAmount() == null || product.getPrice() == null) {
            return;
        }

        double maxExpectedRefund = product.getPrice() * REFUND_TOLERANCE_MULTIPLIER;
        if (dto.getRefundAmount() <= maxExpectedRefund) {
            return;
        }

        DataQualityIssue issue = DataQualityIssue.builder()
                .issueType(IssueType.INVALID_REFUND_AMOUNT)
                .referenceId(dto.getProductId())
                .referenceType("ReturnRequest")
                .description("Refund amount exceeds 120% of product price")
                .build();
        dataQualityIssueRepository.save(issue);
    }

    private void updateCustomerReturnStats(Customer customer, Double refundAmount) {
        customer.setTotalReturns(valueOrZero(customer.getTotalReturns()) + 1);
        customer.setTotalRefundAmount(valueOrZero(customer.getTotalRefundAmount()) + valueOrZero(refundAmount));

        if (customer.getTotalReturns() > SUSPICIOUS_RETURN_COUNT_THRESHOLD
                || customer.getTotalRefundAmount() > SUSPICIOUS_REFUND_AMOUNT_THRESHOLD) {
            customer.setFlaggedAsSuspicious(true);
        }

        customerRepository.save(customer);
    }

    private void updateProductReturnStats(Product product, Integer quantity) {
        int totalReturned = valueOrZero(product.getTotalReturned()) + valueOrZero(quantity);
        int totalSold = valueOrZero(product.getTotalSold());

        product.setTotalReturned(totalReturned);
        product.setReturnRate(totalSold == 0 ? 0.0 : (totalReturned * 100.0) / totalSold);
        productRepository.save(product);
    }

    private Integer valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    private Double valueOrZero(Double value) {
        return value == null ? 0.0 : value;
    }
}
