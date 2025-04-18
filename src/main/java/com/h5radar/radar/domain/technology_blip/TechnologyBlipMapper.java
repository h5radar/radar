package com.h5radar.radar.domain.technology_blip;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.radar.Radar;
import com.h5radar.radar.domain.radar.RadarRepository;
import com.h5radar.radar.domain.ring.Ring;
import com.h5radar.radar.domain.ring.RingRepository;
import com.h5radar.radar.domain.segment.Segment;
import com.h5radar.radar.domain.segment.SegmentRepository;
import com.h5radar.radar.domain.technology.Technology;
import com.h5radar.radar.domain.technology.TechnologyRepository;

@Mapper(config = MapperConfiguration.class)
public abstract class TechnologyBlipMapper implements PlainMapper<TechnologyBlip, TechnologyBlipDto> {
  @Autowired
  protected RadarRepository radarRepository;

  @Autowired
  protected TechnologyRepository technologyRepository;

  @Autowired
  protected SegmentRepository segmentRepository;

  @Autowired
  protected RingRepository ringRepository;

  @Mapping(source = "radar.id", target = "radarId")
  @Mapping(source = "radar.title", target = "radarTitle")
  @Mapping(source = "technology.id", target = "technologyId")
  @Mapping(source = "technology.title", target = "technologyTitle")
  @Mapping(source = "technology.website", target = "technologyWebsite")
  @Mapping(source = "technology.moved", target = "technologyMoved")
  @Mapping(source = "technology.active", target = "technologyActive")
  @Mapping(source = "segment.id", target = "segmentId")
  @Mapping(source = "segment.title", target = "segmentTitle")
  @Mapping(source = "segment.position", target = "segmentPosition")
  @Mapping(source = "ring.id", target = "ringId")
  @Mapping(source = "ring.title", target = "ringTitle")
  @Mapping(source = "ring.position", target = "ringPosition")
  public abstract TechnologyBlipDto toDto(final TechnologyBlip entity);

  @Mapping(target = "radar", expression = "java(getRadar(dto))")
  @Mapping(target = "technology", expression = "java(getTechnology(dto))")
  @Mapping(target = "segment", expression = "java(getSegment(dto))")
  @Mapping(target = "ring", expression = "java(getRing(dto))")
  public abstract TechnologyBlip toEntity(final TechnologyBlipDto dto);

  Radar getRadar(TechnologyBlipDto technologyBlipDto) {
    if (technologyBlipDto.getRadarId() != null) {
      return radarRepository.findById(technologyBlipDto.getRadarId()).get();
    }
    return null;
  }

  Technology getTechnology(TechnologyBlipDto technologyBlipDto) {
    if (technologyBlipDto.getTechnologyId() != null) {
      return technologyRepository.findById(technologyBlipDto.getTechnologyId()).get();
    }
    return null;
  }

  Segment getSegment(TechnologyBlipDto technologyBlipDto) {
    if (technologyBlipDto.getSegmentId() != null) {
      return segmentRepository.findById(technologyBlipDto.getSegmentId()).get();
    }
    return null;
  }

  Ring getRing(TechnologyBlipDto technologyBlipDto) {
    if (technologyBlipDto.getRingId() != null) {
      return ringRepository.findById(technologyBlipDto.getRingId()).get();
    }
    return null;
  }
}

