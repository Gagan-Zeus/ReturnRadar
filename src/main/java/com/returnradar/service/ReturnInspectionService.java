package com.returnradar.service;

import com.returnradar.dto.request.ReturnInspectionRequest;
import com.returnradar.dto.response.ReturnInspectionResponse;
import java.util.List;

public interface ReturnInspectionService {

    ReturnInspectionResponse createInspection(ReturnInspectionRequest request);

    List<ReturnInspectionResponse> getAllInspections();

    ReturnInspectionResponse getInspectionByReturnId(Long returnId);
}
