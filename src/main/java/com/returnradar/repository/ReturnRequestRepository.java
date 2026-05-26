package com.returnradar.repository;

import com.returnradar.entity.ReturnRequest;
import com.returnradar.enums.ReturnReason;
import com.returnradar.enums.ReturnStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {

    boolean existsByOrderIdAndProductId(Long orderId, Long productId);

    List<ReturnRequest> findByCustomerId(Long customerId);

    List<ReturnRequest> findByCity(String city);

    List<ReturnRequest> findByReturnStatus(ReturnStatus status);

    @Query("select returnRequest from ReturnRequest returnRequest where returnRequest.returnReason = :reason")
    List<ReturnRequest> findByReason(@Param("reason") ReturnReason reason);
}
