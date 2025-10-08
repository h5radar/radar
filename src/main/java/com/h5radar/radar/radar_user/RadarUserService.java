package com.h5radar.radar.radar_user;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RadarUserService {

  Collection<RadarUserDto> findAll();

  Page<RadarUserDto> findAll(RadarUserFilter radarUserFilter, Pageable pageable);

  Optional<RadarUserDto> findById(Long id);

  Optional<RadarUserDto> findBySub(String sub);

  RadarUserDto save(RadarUserDto radarUserDto);

  void updateSeed(Long id);

  void deleteById(Long id);
}
