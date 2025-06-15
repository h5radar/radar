package com.h5radar.radar.domain.license;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long>,
    JpaSpecificationExecutor<License> {
  Optional<License> findByTitle(String title);
}
