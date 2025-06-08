package com.h5radar.radar.domain.radar_user;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RadarUserService {

  Collection<RadarUserDto> findAll();

  Page<RadarUserDto> findAll(RadarUserFilter technologyFilter, Pageable pageable);

  Optional<RadarUserDto> findById(Long id);

  Optional<RadarUserDto> findBySub(String sub);

  RadarUserDto save(RadarUserDto technologyDto);

  void deleteById(Long id);
}
