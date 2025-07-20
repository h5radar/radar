package com.h5radar.radar.maturity;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaturityService {

  Collection<MaturityDto> findAll();

  Page<MaturityDto> findAll(MaturityFilter maturityFilter, Pageable pageable);

  Optional<MaturityDto> findById(Long id);

  Optional<MaturityDto> findByTitle(String title);

  MaturityDto save(MaturityDto maturityDto);

  void deleteById(Long id);

  long deleteByRadarUserId(Long radarUserId);

  long countByRadarUserId(Long radarUserId);

  void seed(Long radarUserId) throws Exception;
}
