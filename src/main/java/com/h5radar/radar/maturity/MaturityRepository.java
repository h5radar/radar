package com.h5radar.radar.maturity;

import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface MaturityRepository extends JpaRepository<Maturity, Long>,
    JpaSpecificationExecutor<Maturity> {
  Optional<Maturity> findByTitle(String title);

  Optional<Maturity> findByRadarUserIdAndTitle(Long radarUserId, String title);

  long countByRadarUserId(@NotNull long radarUserId);

  long deleteByRadarUserId(@NotNull long radarUserId);
}
