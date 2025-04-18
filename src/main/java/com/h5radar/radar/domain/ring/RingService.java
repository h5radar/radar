package com.h5radar.radar.domain.ring;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RingService {

  Collection<RingDto> findAll();

  Page<RingDto> findAll(RingFilter ringFilter, Pageable pageable);

  Optional<RingDto> findById(Long id);

  Optional<RingDto> findByTitle(String title);

  RingDto save(RingDto ringDto);

  void deleteById(Long id);
}
