package com.returnradar.service;

import com.returnradar.dto.request.ReturnInspectionRequest;
import com.returnradar.dto.response.ReturnInspectionResponse;

public interface ReturnInspectionService {

    ReturnInspectionResponse createInspection(ReturnInspectionRequest request);

    ReturnInspectionResponse getInspectionByReturnId(Long returnId);
}
