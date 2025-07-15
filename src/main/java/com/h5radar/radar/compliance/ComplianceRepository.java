package com.h5radar.radar.compliance;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceRepository extends JpaRepository<Compliance, Long>,
    JpaSpecificationExecutor<Compliance> {
  Optional<Compliance> findByTitle(String title);

  Optional<Compliance> findByRadarUserIdAndTitle(Long radarUserId, String title);

  long countByRadarUserId(@NotNull long radarUserId);

  long deleteByRadarUserId(@NotNull long radarUserId);
}
