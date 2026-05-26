package com.returnradar.service.impl;

import com.returnradar.dto.request.ReturnInspectionRequest;
import com.returnradar.dto.response.ReturnInspectionResponse;
import com.returnradar.entity.DataQualityIssue;
import com.returnradar.entity.ReturnInspection;
import com.returnradar.entity.ReturnRequest;
import com.returnradar.enums.IssueType;
import com.returnradar.enums.ReturnStatus;
import com.returnradar.exception.BusinessRuleException;
import com.returnradar.exception.ResourceNotFoundException;
import com.returnradar.mapper.InspectionMapper;
import com.returnradar.repository.DataQualityIssueRepository;
import com.returnradar.repository.ReturnInspectionRepository;
import com.returnradar.repository.ReturnRequestRepository;
import com.returnradar.service.ReturnInspectionService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReturnInspectionServiceImpl implements ReturnInspectionService {

    private final ReturnInspectionRepository returnInspectionRepository;
    private final ReturnRequestRepository returnRequestRepository;
    private final DataQualityIssueRepository dataQualityIssueRepository;

    @Override
    @Transactional
    public ReturnInspectionResponse createInspection(ReturnInspectionRequest request) {
        ReturnRequest returnRequest = returnRequestRepository.findById(request.getReturnId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Return request not found with id: " + request.getReturnId()));

        returnInspectionRepository.findByReturnRequest_ReturnId(request.getReturnId())
                .ifPresent(existingInspection -> {
                    logDuplicateInspection(request.getReturnId());
                    throw new BusinessRuleException("Inspection already exists for this return");
                });

        ReturnInspection inspection = InspectionMapper.toEntity(request, returnRequest);
        inspection.setInspectedAt(LocalDateTime.now());

        if (Boolean.TRUE.equals(request.getApproved())) {
            returnRequest.setReturnStatus(ReturnStatus.INSPECTED);
            returnRequestRepository.save(returnRequest);
        }

        return InspectionMapper.toResponse(returnInspectionRepository.save(inspection));
    }

    @Override
    public List<ReturnInspectionResponse> getAllInspections() {
        return returnInspectionRepository.findAll()
                .stream()
                .map(InspectionMapper::toResponse)
                .toList();
    }

    @Override
    public ReturnInspectionResponse getInspectionByReturnId(Long returnId) {
        return returnInspectionRepository.findByReturnRequest_ReturnId(returnId)
                .map(InspectionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inspection not found for return id: " + returnId));
    }

    private void logDuplicateInspection(Long returnId) {
        DataQualityIssue issue = DataQualityIssue.builder()
                .issueType(IssueType.INCONSISTENT_WAREHOUSE_RECORD)
                .referenceId(returnId)
                .referenceType("ReturnInspection")
                .description("Duplicate inspection attempted for return id: " + returnId)
                .build();
        dataQualityIssueRepository.save(issue);
    }
}
