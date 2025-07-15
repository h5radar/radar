package com.h5radar.radar.domain.compliance;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComplianceService {

  Collection<ComplianceDto> findAll();

  Page<ComplianceDto> findAll(ComplianceFilter complianceFilter, Pageable pageable);

  Optional<ComplianceDto> findById(Long id);

  Optional<ComplianceDto> findByTitle(String title);

  ComplianceDto save(ComplianceDto complianceDto);

  void deleteById(Long id);

  long deleteByRadarUserId(Long radarUserId);

  long countByRadarUserId(Long radarUserId);

  void seed(Long radarUserId) throws Exception;
}
