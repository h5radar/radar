package com.h5radar.radar.domain.segment;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractServiceTests;
import com.h5radar.radar.domain.ValidationException;
import com.h5radar.radar.domain.radar.Radar;
import com.h5radar.radar.domain.radar.RadarRepository;

class SegmentServiceTests extends AbstractServiceTests {
  @MockitoBean
  private SegmentRepository segmentRepository;

  @MockitoBean
  private RadarRepository radarRepository;

  @Autowired
  private SegmentMapper segmentMapper;

  @Autowired
  private SegmentService segmentService;

  @Test
  void shouldFindAllSegments() {
    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setRadar(null);
    segment.setTitle("My segment");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    List<Segment> segmentList = List.of(segment);
    Mockito.when(segmentRepository.findAll(any(Sort.class))).thenReturn(segmentList);

    Collection<SegmentDto> segmentDtoCollection = segmentService.findAll();
    Assertions.assertEquals(1, segmentDtoCollection.size());
    Assertions.assertEquals(segmentDtoCollection.iterator().next().getId(), segment.getId());
    Assertions.assertEquals(segmentDtoCollection.iterator().next().getTitle(), segment.getTitle());
    Assertions.assertEquals(segmentDtoCollection.iterator().next().getDescription(), segment.getDescription());
  }

  @Test
  void shouldFindAllSegmentsWithNullFilter() {
    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setRadar(null);
    segment.setTitle("My segment");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    List<Segment> segmentList = List.of(segment);
    Page<Segment> page = new PageImpl<>(segmentList);
    Mockito.when(segmentRepository.findAll(ArgumentMatchers.<Specification<Segment>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<SegmentDto> segmentDtoPage = segmentService.findAll(null, pageable);
    Assertions.assertEquals(1, segmentDtoPage.getSize());
    Assertions.assertEquals(0, segmentDtoPage.getNumber());
    Assertions.assertEquals(1, segmentDtoPage.getTotalPages());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getId(), segment.getId());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getTitle(), segment.getTitle());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getDescription(), segment.getDescription());

    // Mockito.verify(segmentRepository).findAll(
    //    Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindAllSegmentsWithEmptyFilter() {
    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setRadar(null);
    segment.setTitle("My segment");
    segment.setDescription("My segment description");
    segment.setPosition(1);

    List<Segment> segmentList = List.of(segment);
    Page<Segment> page = new PageImpl<>(segmentList);
    Mockito.when(segmentRepository.findAll(ArgumentMatchers.<Specification<Segment>>any(), any(Pageable.class)))
        .thenReturn(page);

    SegmentFilter segmentFilter = new SegmentFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<SegmentDto> segmentDtoPage = segmentService.findAll(segmentFilter, pageable);
    Assertions.assertEquals(1, segmentDtoPage.getSize());
    Assertions.assertEquals(0, segmentDtoPage.getNumber());
    Assertions.assertEquals(1, segmentDtoPage.getTotalPages());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getId(), segment.getId());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getTitle(), segment.getTitle());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getDescription(), segment.getDescription());

    // Mockito.verify(segmentRepository).findAll(
    //    Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  /* TODO

  @Test
  @Transactional
  void shouldFindAllSegmentsWithBlankTitleFilter() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("My radar type title");
    radarType.setDescription("My radar type description");
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarTypeRepository.saveAndFlush(radarType);

    final Radar radar = new Radar();
    radar.setTitle("My radar title");
    radar.setRadarType(radarType);
    radar.setDescription("My radar description");
    radar.setPrimary(false);
    radar.setActive(false);
    radarType.setRadarList(List.of(radar));
    radarRepository.saveAndFlush(radar);

    List<Segment> segmentList = List.of(
        new Segment(null, radar, "My first segment title", "Description", 0, null),
        new Segment(null, radar, "My second segment title", "New description", 1, null)
    );
    for (Segment segment : segmentList) {
      segmentRepository.save(segment);
    }

    SegmentFilter segmentFilter = new SegmentFilter();
    segmentFilter.setTitle("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<SegmentDto> segmentDtoPage = segmentService.findAll(segmentFilter, pageable);
    Assertions.assertEquals(10, segmentDtoPage.getSize());
    Assertions.assertEquals(0, segmentDtoPage.getNumber());
    Assertions.assertEquals(1, segmentDtoPage.getTotalPages());
    Assertions.assertEquals(2, segmentDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllSegmentsWithTitleFilter() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("My radar type title");
    radarType.setDescription("My radar type description");
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarTypeRepository.saveAndFlush(radarType);

    final Radar radar = new Radar();
    radar.setTitle("My radar title");
    radar.setRadarType(radarType);
    radar.setDescription("My radar description");
    radar.setPrimary(false);
    radar.setActive(false);
    radarType.setRadarList(List.of(radar));
    radarRepository.saveAndFlush(radar);

    List<Segment> segmentList = List.of(
        new Segment(null, radar, "My first segment title", "Description", 0, null),
        new Segment(null, radar, "My second segment title", "New description", 1, null)
    );
    for (Segment segment : segmentList) {
      segmentRepository.save(segment);
    }

    SegmentFilter segmentFilter = new SegmentFilter();
    segmentFilter.setTitle(segmentList.iterator().next().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<SegmentDto> segmentDtoPage = segmentService.findAll(segmentFilter, pageable);
    Assertions.assertEquals(10, segmentDtoPage.getSize());
    Assertions.assertEquals(0, segmentDtoPage.getNumber());
    Assertions.assertEquals(1, segmentDtoPage.getTotalPages());
    Assertions.assertEquals(1, segmentDtoPage.getNumberOfElements());
    Assertions.assertNotNull(segmentDtoPage.iterator().next().getId());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getTitle(), segmentList.iterator().next().getTitle());
    Assertions.assertEquals(segmentDtoPage.iterator().next().getDescription(),
        segmentList.iterator().next().getDescription());
  }
   */

  @Test
  void shouldFindByIdSegment() {
    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setTitle("My title");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(segmentRepository.findById(segment.getId())).thenReturn(Optional.of(segment));

    Optional<SegmentDto> segmentDtoOptional = segmentService.findById(segment.getId());
    Assertions.assertTrue(segmentDtoOptional.isPresent());
    Assertions.assertEquals(segment.getId(), segmentDtoOptional.get().getId());
    Assertions.assertEquals(segment.getTitle(), segmentDtoOptional.get().getTitle());
    Assertions.assertEquals(segment.getDescription(), segmentDtoOptional.get().getDescription());
    Assertions.assertEquals(segment.getPosition(), segmentDtoOptional.get().getPosition());
    Mockito.verify(segmentRepository).findById(segment.getId());
  }

  @Test
  void shouldFindByTitleSegment() {
    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setTitle("My title");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(segmentRepository.findByTitle(segment.getTitle())).thenReturn(Optional.of(segment));

    Optional<SegmentDto> segmentDtoOptional = segmentService.findByTitle(segment.getTitle());
    Assertions.assertTrue(segmentDtoOptional.isPresent());
    Assertions.assertEquals(segment.getId(), segmentDtoOptional.get().getId());
    Assertions.assertEquals(segment.getTitle(), segmentDtoOptional.get().getTitle());
    Assertions.assertEquals(segment.getDescription(), segmentDtoOptional.get().getDescription());
    Assertions.assertEquals(segment.getPosition(), segmentDtoOptional.get().getPosition());
    Mockito.verify(segmentRepository).findByTitle(segment.getTitle());
  }

  @Test
  void shouldSaveSegment() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(radar);
    segment.setTitle("My title");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(segmentRepository.save(any())).thenReturn(segment);
    Mockito.when(radarRepository.findById(radar.getId())).thenReturn(Optional.of(radar));

    SegmentDto segmentDto = segmentService.save(segmentMapper.toDto(segment));
    Assertions.assertEquals(segment.getId(), segmentDto.getId());
    Assertions.assertEquals(segment.getTitle(), segmentDto.getTitle());
    Assertions.assertEquals(segment.getDescription(), segmentDto.getDescription());
    Assertions.assertEquals(segment.getPosition(), segmentDto.getPosition());

    Mockito.verify(segmentRepository).save(any());
    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
  }

  @Test
  void shouldFailToSaveSegmentDueToRadarIsActive() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(true);

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(radar);
    segment.setTitle("My title");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(radarRepository.findById(radar.getId())).thenReturn(Optional.of(radar));
    Mockito.when(segmentRepository.findById(segment.getId())).thenReturn(Optional.of(segment));

    ValidationException exception =
        catchThrowableOfType(() -> segmentService.save(segmentMapper.toDto(segment)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
    Mockito.verify(segmentRepository).findById(segment.getId());
  }

  @Test
  void shouldFailToSaveSegmentDueToBelongActiveRadar() {
    final Radar radar = new Radar();
    radar.setId(2L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    final Radar radarActive = new Radar();
    radarActive.setId(1L);
    radarActive.setTitle("My radar title");
    radarActive.setDescription("My radar description");
    radarActive.setPrimary(true);
    radarActive.setActive(true);

    final Segment segment = new Segment();
    segment.setId(3L);
    segment.setRadar(radarActive);
    segment.setTitle("ADOPT");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(radarRepository.findById(segment.getRadar().getId())).thenReturn(Optional.of(radar));
    Mockito.when(segmentRepository.findById(segment.getId())).thenReturn(Optional.of(segment));

    ValidationException exception =
            catchThrowableOfType(() -> segmentService.save(segmentMapper.toDto(segment)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("can't be saved for active radar"));

    Mockito.verify(radarRepository, Mockito.times(2)).findById(radarActive.getId());
  }

  @Test
  void shouldFailToSaveSegmentDueToTitleWithWhiteSpace() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Segment segment = new Segment();
    segment.setId(2L);
    segment.setRadar(radar);
    segment.setTitle(" My title ");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    ValidationException exception =
        catchThrowableOfType(() -> segmentService.save(segmentMapper.toDto(segment)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
  }

  @Test
  void shouldDeleteSegment() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setRadar(radar);
    segment.setTitle("My title");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(segmentRepository.findById(any())).thenReturn(Optional.of(segment));
    Mockito.doAnswer((i) -> null).when(segmentRepository).deleteById(segment.getId());

    segmentService.deleteById(segment.getId());
    Mockito.verify(segmentRepository).findById(segment.getId());
    Mockito.verify(segmentRepository).deleteById(segment.getId());
  }

  @Test
  void shouldFailToDeleteSegmentDueToRadarIsActive() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Segment segment = new Segment();
    segment.setId(10L);
    segment.setRadar(radar);
    segment.setTitle("My title");
    segment.setDescription("My description");
    segment.setPosition(1);

    Mockito.when(segmentRepository.findById(any())).thenReturn(Optional.of(segment));

    ValidationException exception =
        catchThrowableOfType(() -> segmentService.deleteById(segment.getId()), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertEquals(exception.getMessage(), "Segment can't be deleted for active radar.");
    Assertions.assertTrue(segment.getId().describeConstable().isPresent());

    Mockito.verify(segmentRepository).findById(segment.getId());
  }
}
