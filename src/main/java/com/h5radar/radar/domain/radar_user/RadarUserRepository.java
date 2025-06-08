package com.h5radar.radar.domain.radar_user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RadarUserRepository extends JpaRepository<RadarUser, Long>,
    JpaSpecificationExecutor<RadarUser> {
  Optional<RadarUser> findBySub(String sub);
}
