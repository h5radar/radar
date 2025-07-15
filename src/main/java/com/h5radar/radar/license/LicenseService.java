package com.h5radar.radar.license;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LicenseService {

  Collection<LicenseDto> findAll();

  Page<LicenseDto> findAll(LicenseFilter licenseFilter, Pageable pageable);

  Optional<LicenseDto> findById(Long id);

  Optional<LicenseDto> findByTitle(String title);

  LicenseDto save(LicenseDto licenseDto);

  void deleteById(Long id);

  long deleteByRadarUserId(Long radarUserId);

  long countByRadarUserId(Long radarUserId);

  void seed(Long radarUserId) throws Exception;
}
