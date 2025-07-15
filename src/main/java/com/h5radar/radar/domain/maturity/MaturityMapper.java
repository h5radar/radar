package com.h5radar.radar.domain.maturity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spmaturityframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.radar.Radar;
import com.h5radar.radar.domain.radar.RadarRepository;
import com.h5radar.radar.domain.technology_blip.TechnologyBlipMapper;

@Mapper(config = MapperConfiguration.class,
    uses = {TechnologyBlipMapper.class})
public abstract class MaturityMapper implements PlainMapper<Maturity, MaturityDto> {

  @Autowired
  protected RadarRepository radarRepository;

  @Mapping(source = "radar.id", target = "radarId")
  @Mapping(source = "radar.title", target = "radarTitle")
  @Mapping(source = "technologyBlipList", target = "technologyBlipDtoList")
  public abstract MaturityDto toDto(final Maturity entity);

  @Mapping(target = "radar", expression = "java(getRadar(dto))")
  @Mapping(source = "technologyBlipDtoList", target = "technologyBlipList")
  public abstract Maturity toEntity(final MaturityDto dto);

  Radar getRadar(MaturityDto maturityDto) {
    if (maturityDto.getRadarId() != null) {
      return radarRepository.findById(maturityDto.getRadarId()).get();
    }
    return null;
  }
}
