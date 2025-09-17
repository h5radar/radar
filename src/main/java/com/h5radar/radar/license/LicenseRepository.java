package com.h5radar.radar.license;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long>,
    JpaSpecificationExecutor<License> {
  Optional<License> findByTitle(String title);

  Optional<License> findByRadarUserIdAndTitle(Long radarUserId, String title);

  long countByRadarUserId(@NotNull long radarUserId);

  long deleteByRadarUserId(@NotNull long radarUserId);

  @Query("""
      select c.id, c.title, count(l.id)
      from License l
      left join l.compliance c
      where l.radarUser.id = :radarUserId
      group by c.id, c.title
      """)
  List<Object[]> groupByComplianceRaw(@Param("radarUserId") Long radarUserId);
}
