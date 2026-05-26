package com.returnradar.repository;

import com.returnradar.entity.DataQualityIssue;
import com.returnradar.enums.IssueType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataQualityIssueRepository extends JpaRepository<DataQualityIssue, Long> {

    List<DataQualityIssue> findByResolvedFalse();

    List<DataQualityIssue> findByIssueType(IssueType type);
}
