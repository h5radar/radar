package com.h5radar.radar.domain.technology;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TechnologyService {

  Collection<TechnologyDto> findAll();

  Page<TechnologyDto> findAll(TechnologyFilter technologyFilter, Pageable pageable);

  Optional<TechnologyDto> findById(Long id);

  Optional<TechnologyDto> findByTitle(String title);

  TechnologyDto save(TechnologyDto technologyDto);

  void deleteById(Long id);

  long deleteByRadarUserId(Long radarUserId);

  long countByRadarUserId(Long radarUserId);

  void seed(Long radarUserId) throws Exception;
}
