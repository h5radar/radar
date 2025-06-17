package com.h5radar.radar.domain.practice;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PracticeService {

  Collection<PracticeDto> findAll();

  Page<PracticeDto> findAll(PracticeFilter practiceFilter, Pageable pageable);

  Optional<PracticeDto> findById(Long id);

  Optional<PracticeDto> findByTitle(String title);

  Optional<PracticeDto> findByRadarUserIdAndTitle(Long radarUserId, String title);

  PracticeDto save(PracticeDto practiceDto);

  void deleteById(Long id);

  long deleteByRadarUserId(Long radarUserId);

  long countByRadarUserId(Long radarUserId);

  void seed(Long radarUserId) throws Exception;
}
