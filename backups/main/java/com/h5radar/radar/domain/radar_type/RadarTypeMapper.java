package com.h5radar.radar.domain.radar_type;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public abstract class RadarTypeMapper implements PlainMapper<RadarType, RadarTypeDto> {

  public abstract RadarTypeDto toDto(final RadarType entity);

  @Mapping(target = "radarList", ignore = true)
  public abstract RadarType toEntity(final RadarTypeDto dto);
}
