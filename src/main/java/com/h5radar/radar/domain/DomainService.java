package com.h5radar.radar.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DomainService {

  Collection<DomainDto> findAll();

  Page<DomainDto> findAll(DomainFilter domainFilter, Pageable pageable);

  Optional<DomainDto> findById(Long id);

  Optional<DomainDto> findByTitle(String title);

  DomainDto save(DomainDto domainDto);

  void deleteById(Long id);

  long deleteByRadarUserId(Long radarUserId);

  long countByRadarUserId(Long radarUserId);

  void seed(Long radarUserId) throws Exception;
}
