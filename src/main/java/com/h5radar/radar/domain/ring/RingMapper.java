package com.h5radar.radar.domain.ring;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.radar.Radar;
import com.h5radar.radar.domain.radar.RadarRepository;
import com.h5radar.radar.domain.technology_blip.TechnologyBlipMapper;

@Mapper(config = MapperConfiguration.class,
    uses = {TechnologyBlipMapper.class})
public abstract class RingMapper implements PlainMapper<Ring, RingDto> {

  @Autowired
  protected RadarRepository radarRepository;

  @Mapping(source = "radar.id", target = "radarId")
  @Mapping(source = "radar.title", target = "radarTitle")
  @Mapping(source = "technologyBlipList", target = "technologyBlipDtoList")
  public abstract RingDto toDto(final Ring entity);

  @Mapping(target = "radar", expression = "java(getRadar(dto))")
  @Mapping(source = "technologyBlipDtoList", target = "technologyBlipList")
  public abstract Ring toEntity(final RingDto dto);

  Radar getRadar(RingDto ringDto) {
    if (ringDto.getRadarId() != null) {
      return radarRepository.findById(ringDto.getRadarId()).get();
    }
    return null;
  }
}
