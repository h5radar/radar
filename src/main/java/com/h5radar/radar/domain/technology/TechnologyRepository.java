package com.h5radar.radar.domain.technology;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.h5radar.radar.domain.radar_user.RadarUser;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long>,
    JpaSpecificationExecutor<Technology> {
  Optional<Technology> findByTitle(String title);

  long countByRadarUser(@NotNull RadarUser radarUser);
}
