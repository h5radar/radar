package com.h5radar.radar.domain.technology;

import java.util.Optional;

import com.h5radar.radar.domain.radar_user.RadarUser;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long>,
    JpaSpecificationExecutor<Technology> {
  Optional<Technology> findByTitle(String title);

  long countByRadarUser(@NotNull RadarUser radarUser);
}
