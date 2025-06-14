package com.h5radar.radar.domain.radar_user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.technology_blip.TechnologyBlipMapper;

@Mapper(config = MapperConfiguration.class,
    uses = {TechnologyBlipMapper.class})
public abstract class RadarUserMapper implements PlainMapper<RadarUser, RadarUserDto> {

  @Mapping(source = "technologyList", target = "technologyDtoList")
  public abstract RadarUserDto toDto(final RadarUser entity);

  @Mapping(source = "technologyDtoList", target = "technologyList")
  public abstract RadarUser toEntity(final RadarUserDto dto);
}
