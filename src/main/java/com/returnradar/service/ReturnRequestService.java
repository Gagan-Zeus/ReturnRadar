package com.returnradar.service;

import com.returnradar.dto.request.ReturnRequestDto;
import com.returnradar.dto.response.ReturnRequestResponse;
import com.returnradar.enums.ReturnStatus;
import java.util.List;

public interface ReturnRequestService {

    ReturnRequestResponse createReturn(ReturnRequestDto dto);

    ReturnRequestResponse getReturnById(Long id);

    List<ReturnRequestResponse> getAllReturns();

    ReturnRequestResponse updateReturnStatus(Long id, ReturnStatus newStatus);
}
