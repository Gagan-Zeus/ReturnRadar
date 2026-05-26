package com.returnradar.repository;

import com.returnradar.entity.AnalyticsSnapshot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsSnapshotRepository extends JpaRepository<AnalyticsSnapshot, Long> {

    Optional<AnalyticsSnapshot> findTopByOrderBySnapshotTimeDesc();
}
