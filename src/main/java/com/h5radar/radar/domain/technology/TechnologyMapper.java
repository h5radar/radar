package com.h5radar.radar.domain.technology;

import org.mapstruct.Mapper;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface TechnologyMapper extends PlainMapper<Technology, TechnologyDto> {
}
