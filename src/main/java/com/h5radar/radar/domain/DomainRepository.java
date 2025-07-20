package com.h5radar.radar.domain;

import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>,
    JpaSpecificationExecutor<Domain> {
  Optional<Domain> findByTitle(String title);

  Optional<Domain> findByRadarUserIdAndTitle(Long radarUserId, String title);

  long countByRadarUserId(@NotNull long radarUserId);

  long deleteByRadarUserId(@NotNull long radarUserId);
}

