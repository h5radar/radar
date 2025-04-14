package com.t9radar.radar.domain.technology;

import org.mapstruct.Mapper;

import com.t9radar.radar.config.MapperConfiguration;
import com.t9radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface TechnologyMapper extends PlainMapper<Technology, TechnologyDto> {
}
