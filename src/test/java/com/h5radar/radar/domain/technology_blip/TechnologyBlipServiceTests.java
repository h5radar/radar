package com.h5radar.radar.domain.technology_blip;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractServiceTests;
import com.h5radar.radar.domain.ValidationException;
import com.h5radar.radar.domain.radar.Radar;
import com.h5radar.radar.domain.radar.RadarRepository;
import com.h5radar.radar.domain.ring.Ring;
import com.h5radar.radar.domain.ring.RingRepository;
import com.h5radar.radar.domain.segment.Segment;
import com.h5radar.radar.domain.segment.SegmentRepository;
import com.h5radar.radar.domain.technology.Technology;
import com.h5radar.radar.domain.technology.TechnologyRepository;

class TechnologyBlipServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RingRepository ringRepository;
  @MockitoBean
  private RadarRepository radarRepository;
  @MockitoBean
  private SegmentRepository segmentRepository;
  @MockitoBean
  private TechnologyRepository technologyRepository;
  @MockitoBean
  private TechnologyBlipRepository technologyBlipRepository;
  @Autowired
  private TechnologyBlipMapper technologyBlipMapper;
  @Autowired
  private TechnologyBlipService technologyBlipService;

  @Test
  void shouldFindAllTechnologyBlips() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar");
    radar.setDescription("My radar description");

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(null);
    segment.setTitle("My segment title");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(null);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setPosition(1);
    ring.setColor("#fbdb84");

    final Technology technology = new Technology();
    technology.setId(4L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(1);
    technology.setActive(true);

    final TechnologyBlip technologyBlip = new TechnologyBlip();
    technologyBlip.setId(5L);
    technologyBlip.setRadar(radar);
    technologyBlip.setRing(ring);
    technologyBlip.setTechnology(technology);
    technologyBlip.setSegment(segment);
    List<TechnologyBlip> technologyBlipList = List.of(technologyBlip);

    Mockito.when(technologyBlipRepository.findAll(any(Sort.class))).thenReturn(technologyBlipList);

    Collection<TechnologyBlipDto> technologyBlipDtoCollection = technologyBlipService.findAll();
    Assertions.assertEquals(1, technologyBlipDtoCollection.size());
    Assertions.assertEquals(technologyBlipDtoCollection.iterator().next().getId(), technologyBlip.getId());
    Assertions.assertEquals(technologyBlipDtoCollection.iterator().next().getRadarId(),
        technologyBlip.getRadar().getId());
    Assertions.assertEquals(technologyBlipDtoCollection.iterator().next().getSegmentId(),
        technologyBlip.getSegment().getId());
    Assertions.assertEquals(technologyBlipDtoCollection.iterator().next().getRingId(),
        technologyBlip.getRing().getId());
    Assertions.assertEquals(technologyBlipDtoCollection.iterator().next().getTechnologyId(),
        technologyBlip.getTechnology().getId());

    Mockito.verify(technologyBlipRepository).findAll(any(Sort.class));
  }

  @Test
  void shouldFindAllTechnologyBlipWithEmptyFilter() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar");
    radar.setDescription("My radar description");

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(radar);
    segment.setTitle("My segment title");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(radar);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setPosition(1);
    ring.setColor("#fbdb84");

    final Technology technology = new Technology();
    technology.setId(4L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(1);
    technology.setActive(true);

    final TechnologyBlip technologyBlip = new TechnologyBlip();
    technologyBlip.setId(5L);
    technologyBlip.setRadar(radar);
    technologyBlip.setRing(ring);
    technologyBlip.setTechnology(technology);
    technologyBlip.setSegment(segment);

    List<TechnologyBlip> technologyBlipList = List.of(technologyBlip);
    Page<TechnologyBlip> page = new PageImpl<>(technologyBlipList);
    Mockito.when(technologyBlipRepository.findAll(any(Pageable.class))).thenReturn(page);

    TechnologyBlipFilter technologyBlipFilter = new TechnologyBlipFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("technology_blips.id,asc"));
    Page<TechnologyBlipDto> technologyBlipDtoPage = technologyBlipService.findAll(technologyBlipFilter, pageable);
    Assertions.assertEquals(1, technologyBlipDtoPage.getSize());
    Assertions.assertEquals(0, technologyBlipDtoPage.getNumber());
    Assertions.assertEquals(1, technologyBlipDtoPage.getSize());
    Assertions.assertEquals(technologyBlipDtoPage.iterator().next().getId(), technologyBlip.getId());
    Assertions.assertEquals(technologyBlipDtoPage.iterator().next().getRadarId(),
        technologyBlip.getRadar().getId());
    Assertions.assertEquals(technologyBlipDtoPage.iterator().next().getSegmentId(),
        technologyBlip.getSegment().getId());
    Assertions.assertEquals(technologyBlipDtoPage.iterator().next().getRingId(),
        technologyBlip.getRing().getId());
    Assertions.assertEquals(technologyBlipDtoPage.iterator().next().getTechnologyId(),
        technologyBlip.getTechnology().getId());

    Mockito.verify(technologyBlipRepository).findAll(any(Pageable.class));
  }

  @Test
  void shouldFindByIdTechnologyBlips() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar");
    radar.setDescription("My radar description");

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(null);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setPosition(1);
    ring.setColor("#fbdb84");

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(null);
    segment.setTitle("My segment title");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    final Technology technology = new Technology();
    technology.setId(4L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(1);
    technology.setActive(true);

    final TechnologyBlip technologyBlip = new TechnologyBlip();
    technologyBlip.setId(5L);
    technologyBlip.setRadar(radar);
    technologyBlip.setRing(ring);
    technologyBlip.setTechnology(technology);
    technologyBlip.setSegment(segment);

    Mockito.when(technologyBlipRepository.findById(any())).thenReturn(Optional.of(technologyBlip));

    Optional<TechnologyBlipDto> technologyBlipDtoOptional = technologyBlipService.findById(technologyBlip.getId());
    Assertions.assertEquals(technologyBlip.getId(), technologyBlipDtoOptional.get().getId());
    Assertions.assertEquals(technologyBlip.getRadar().getId(), technologyBlipDtoOptional.get().getRadarId());
    Assertions.assertEquals(technologyBlip.getRing().getId(), technologyBlipDtoOptional.get().getRingId());
    Assertions.assertEquals(technologyBlip.getSegment().getId(), technologyBlipDtoOptional.get().getSegmentId());
    Assertions.assertEquals(technologyBlip.getTechnology().getId(), technologyBlipDtoOptional.get().getTechnologyId());

    Mockito.verify(technologyBlipRepository).findById(technologyBlip.getId());
  }

  @Test
  void shouldSaveTechnologyBlipDto() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar");
    radar.setDescription("My radar description");

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(null);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setPosition(1);
    ring.setColor("#fbdb84");

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(null);
    segment.setTitle("My segment title");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    final Technology technology = new Technology();
    technology.setId(4L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(1);
    technology.setActive(true);

    final TechnologyBlip technologyBlip = new TechnologyBlip();
    technologyBlip.setId(5L);
    technologyBlip.setRadar(radar);
    technologyBlip.setRing(ring);
    technologyBlip.setTechnology(technology);
    technologyBlip.setSegment(segment);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(segmentRepository.findById(any())).thenReturn(Optional.of(segment));
    Mockito.when(ringRepository.findById(any())).thenReturn(Optional.of(ring));
    Mockito.when(technologyRepository.findById(any())).thenReturn(Optional.of(technology));
    Mockito.when(technologyBlipRepository.save(any())).thenReturn(technologyBlip);

    TechnologyBlipDto technologyBlipDto = technologyBlipService.save(technologyBlipMapper.toDto(technologyBlip));

    Assertions.assertEquals(technologyBlip.getId(), technologyBlipDto.getId());
    Assertions.assertEquals(technologyBlip.getRadar().getId(), technologyBlipDto.getRadarId());
    Assertions.assertEquals(technologyBlip.getSegment().getId(), technologyBlipDto.getSegmentId());
    Assertions.assertEquals(technologyBlip.getRing().getId(), technologyBlipDto.getRingId());
    Assertions.assertEquals(technologyBlip.getTechnology().getId(), technologyBlipDto.getTechnologyId());

    Mockito.verify(technologyBlipRepository).save(any());
    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(ringRepository).findById(ring.getId());
    Mockito.verify(segmentRepository).findById(segment.getId());
    Mockito.verify(technologyRepository).findById(technology.getId());
  }

  @Test
  void shouldFailToSaveTechnologyBlipDueToRadarAndTechnologyIsNotUnique() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar");
    radar.setDescription("My radar description");

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(null);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setPosition(1);
    ring.setColor("#fbdb84");

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(null);
    segment.setTitle("My segment title");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    final Technology technology = new Technology();
    technology.setId(2L);
    technology.setTitle("My technology");

    // Create Blips
    final TechnologyBlip technologyBlip = new TechnologyBlip();
    technologyBlip.setId(3L);
    technologyBlip.setRadar(radar);
    technologyBlip.setRing(ring);
    technologyBlip.setTechnology(technology);
    technologyBlip.setSegment(segment);

    // Blip that have already been created
    final TechnologyBlip technologyBlip1 = new TechnologyBlip();
    technologyBlip1.setId(4L);
    technologyBlip1.setRadar(radar);
    technologyBlip1.setRing(ring);
    technologyBlip1.setTechnology(technology);
    technologyBlip1.setSegment(segment);
    List<TechnologyBlip> technologyBlipList = List.of(technologyBlip1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(segmentRepository.findById(any())).thenReturn(Optional.of(segment));
    Mockito.when(ringRepository.findById(any())).thenReturn(Optional.of(ring));
    Mockito.when(technologyRepository.findById(any())).thenReturn(Optional.of(technology));
    Mockito.when(technologyBlipRepository.findByRadarIdAndTechnologyId(
        technologyBlip1.getRadar().getId(), technologyBlip1.getTechnology().getId())).thenReturn(technologyBlipList);

    ValidationException exception = catchThrowableOfType(() ->
        technologyBlipService.save(technologyBlipMapper.toDto(technologyBlip)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("radar and technology should be unique"));

    Mockito.verify(technologyBlipRepository)
        .findByRadarIdAndTechnologyId(technologyBlip.getRadar().getId(), technologyBlip1.getTechnology().getId());
    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(ringRepository).findById(ring.getId());
    Mockito.verify(segmentRepository).findById(segment.getId());
    Mockito.verify(technologyRepository).findById(technology.getId());
  }

  @Test
  void shouldFailToSaveTechnologyBlipDueToRadarAndTechnologyIsEmpty() {
    ValidationException exception = catchThrowableOfType(() ->
        technologyBlipService.save(technologyBlipMapper.toDto(new TechnologyBlip())), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("must not be null"));
  }

  @Test
  void shouldDeleteTechnologyBlip() {
    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setTitle("My radar");
    radar.setDescription("My radar description");

    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setRadar(radar);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setPosition(1);
    ring.setColor("#fbdb84");

    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setRadar(radar);
    segment.setTitle("My segment title");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(1);
    technology.setActive(true);

    final TechnologyBlip technologyBlip = new TechnologyBlip();
    technologyBlip.setId(10L);
    technologyBlip.setRadar(radar);
    technologyBlip.setRing(ring);
    technologyBlip.setTechnology(technology);
    technologyBlip.setSegment(segment);

    Mockito.doAnswer((i) -> null).when(technologyBlipRepository).deleteById(technologyBlip.getId());

    technologyBlipService.deleteById(technologyBlip.getId());

    Mockito.verify(technologyBlipRepository).deleteById(technologyBlip.getId());
  }
}
