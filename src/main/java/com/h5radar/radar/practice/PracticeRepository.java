package com.h5radar.radar.practice;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long>,
    JpaSpecificationExecutor<Practice> {
  Optional<Practice> findByTitle(String title);

  Optional<Practice> findByRadarUserIdAndTitle(Long radarUserId, String title);

  long countByRadarUserId(@NotNull long radarUserId);

  long deleteByRadarUserId(@NotNull long radarUserId);
}
