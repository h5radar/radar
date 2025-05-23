package com.h5radar.radar.domain.technology_blip;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TechnologyBlipService {

  Collection<TechnologyBlipDto> findAll();

  Page<TechnologyBlipDto> findAll(TechnologyBlipFilter technologyBlipFilter, Pageable pageable);

  Optional<TechnologyBlipDto> findById(Long id);

  TechnologyBlipDto save(TechnologyBlipDto technologyBlipDto);

  void deleteById(Long id);
}
