package com.returnradar.repository;

import com.returnradar.entity.ReturnInspection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnInspectionRepository extends JpaRepository<ReturnInspection, Long> {

    Optional<ReturnInspection> findByReturnRequest_ReturnId(Long returnId);

    List<ReturnInspection> findByApprovedTrue();
}
