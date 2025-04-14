package com.t9radar.radar.domain.technology_blip;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.t9radar.radar.domain.AbstractMapperTests;
import com.t9radar.radar.domain.radar.Radar;
import com.t9radar.radar.domain.radar.RadarRepository;
import com.t9radar.radar.domain.radar_type.RadarType;
import com.t9radar.radar.domain.ring.Ring;
import com.t9radar.radar.domain.ring.RingRepository;
import com.t9radar.radar.domain.segment.Segment;
import com.t9radar.radar.domain.segment.SegmentRepository;
import com.t9radar.radar.domain.technology.Technology;
import com.t9radar.radar.domain.technology.TechnologyRepository;

class TechnologyBlipMapperTests extends AbstractMapperTests {

  @MockitoBean
  private RadarRepository radarRepository;
  @MockitoBean
  private RingRepository ringRepository;
  @MockitoBean
  private SegmentRepository segmentRepository;
  @MockitoBean
  private TechnologyRepository technologyRepository;
  @Autowired
  private TechnologyBlipMapper technologyBlipMapper;

  @Test
  void testToWithNull() {
    final var technologyBlipDto = technologyBlipMapper.toDto(null);

    Assertions.assertNull(technologyBlipDto);
  }

  @Test
  void testToDtoAllFields() {
    final var radarType = new RadarType();
    radarType.setId(10L);
    radarType.setTitle("My radarType title");
    radarType.setDescription("My radarType description");
    radarType.setCode("My radarType code");

    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(radarType);
    radar.setTitle("My radar title");
    radar.setDescription("My radar Description");
    radar.setPrimary(true);
    radar.setActive(true);

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(radar);
    segment.setTitle("My segment title");
    segment.setDescription("My segment Description");
    segment.setPosition(1);

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(radar);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setColor("My ring color");
    ring.setPosition(1);

    final Technology technology = new Technology();
    technology.setId(4L);
    technology.setTitle("My technology title");
    technology.setWebsite("My technology website");
    technology.setDescription("My technology description");
    technology.setMoved(1);
    technology.setActive(true);

    final TechnologyBlip technology_blip = new TechnologyBlip();
    technology_blip.setId(5L);
    technology_blip.setRadar(radar);
    technology_blip.setSegment(segment);
    technology_blip.setRing(ring);
    technology_blip.setTechnology(technology);

    final var technologyBlipDto = technologyBlipMapper.toDto(technology_blip);

    Assertions.assertEquals(technologyBlipDto.getRadarId(), technology_blip.getRadar().getId());
    Assertions.assertEquals(technologyBlipDto.getSegmentId(), technology_blip.getSegment().getId());
    Assertions.assertEquals(technologyBlipDto.getRingId(), technology_blip.getRing().getId());
    Assertions.assertEquals(technologyBlipDto.getTechnologyId(), technology_blip.getTechnology().getId());
  }

  @Test
  void testToEntityWithNull() {
    final var technology_blip = technologyBlipMapper.toEntity(null);

    Assertions.assertNull(technology_blip);
  }

  @Test
  void testToEntityAllFields() {
    final var radarType = new RadarType();
    radarType.setId(10L);
    radarType.setTitle("My radarType title");
    radarType.setDescription("My radarType description");
    radarType.setCode("My radarType code");

    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(radarType);
    radar.setTitle("My radar title");
    radar.setDescription("My radar Description");
    radar.setPrimary(true);
    radar.setActive(true);

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(radar);
    segment.setTitle("My segment title");
    segment.setDescription("My segment Description");
    segment.setPosition(1);

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(radar);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setColor("My ring color");
    ring.setPosition(1);

    final Technology technology = new Technology();
    technology.setId(4L);
    technology.setTitle("My technology title");
    technology.setWebsite("My technology website");
    technology.setDescription("My technology description");
    technology.setMoved(1);
    technology.setActive(true);

    final TechnologyBlipDto technology_blipDto = new TechnologyBlipDto();
    technology_blipDto.setId(5L);
    technology_blipDto.setRadarId(radar.getId());
    technology_blipDto.setSegmentId(segment.getId());
    technology_blipDto.setRingId(ring.getId());
    technology_blipDto.setTechnologyId(technology.getId());

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(ringRepository.findById(any())).thenReturn(Optional.of(ring));
    Mockito.when(segmentRepository.findById(any())).thenReturn(Optional.of(segment));
    Mockito.when(technologyRepository.findById(any())).thenReturn(Optional.of(technology));

    final var technology_blip = technologyBlipMapper.toEntity(technology_blipDto);

    Assertions.assertEquals(technology_blip.getId(), technology_blipDto.getId());
    Assertions.assertEquals(technology_blip.getRadar().getId(), technology_blipDto.getRadarId());
    Assertions.assertEquals(technology_blip.getSegment().getId(), technology_blipDto.getSegmentId());
    Assertions.assertEquals(technology_blip.getRing().getId(), technology_blipDto.getRingId());
    Assertions.assertEquals(technology_blip.getTechnology().getId(), technology_blipDto.getTechnologyId());

    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(ringRepository).findById(ring.getId());
    Mockito.verify(segmentRepository).findById(segment.getId());
    Mockito.verify(technologyRepository).findById(technology.getId());
  }
}