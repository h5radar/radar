package com.h5radar.radar.domain.radar;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

import java.util.Collection;
import java.util.LinkedList;
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
import com.h5radar.radar.domain.radar_type.RadarType;
import com.h5radar.radar.domain.radar_type.RadarTypeRepository;
import com.h5radar.radar.domain.ring.Ring;
import com.h5radar.radar.domain.segment.Segment;

class RadarServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RadarTypeRepository radarTypeRepository;
  @MockitoBean
  private RadarRepository radarRepository;
  @Autowired
  private RadarMapper radarMapper;
  @Autowired
  private RadarService radarService;

  @Test
  void shouldFindAllRadars() {
    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar Description");

    List<Radar> radarList = List.of(radar);
    Mockito.when(radarRepository.findAll(any(Sort.class))).thenReturn(radarList);

    Collection<RadarDto> radarDtoCollection = radarService.findAll();
    Assertions.assertEquals(1, radarDtoCollection.size());
    Assertions.assertEquals(radarDtoCollection.iterator().next().getId(), radar.getId());
    Assertions.assertEquals(radarDtoCollection.iterator().next().getTitle(), radar.getTitle());
    Assertions.assertEquals(radarDtoCollection.iterator().next().getDescription(), radar.getDescription());
  }

  @Test
  void shouldFindAllRadarsWithNullFilter() {
    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar Description");

    List<Radar> radarList = List.of(radar);
    Page<Radar> page = new PageImpl<>(radarList);
    Mockito.when(radarRepository.findAll(ArgumentMatchers.<Specification<Radar>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title, asc"));
    Page<RadarDto> radarDtoPage = radarService.findAll(null, pageable);
    Assertions.assertEquals(1, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(radarDtoPage.iterator().next().getId(), radar.getId());
    Assertions.assertEquals(radarDtoPage.iterator().next().getTitle(), radar.getTitle());
    Assertions.assertEquals(radarDtoPage.iterator().next().getDescription(), radar.getDescription());

    // Mockito.verify(radarRepository).findAll(Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindAllRadarsWithEmptyFilter() {
    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar Description");

    List<Radar> radarList = List.of(radar);
    Page<Radar> page = new PageImpl<>(radarList);
    Mockito.when(radarRepository.findAll(ArgumentMatchers.<Specification<Radar>>any(), any(Pageable.class)))
        .thenReturn(page);

    RadarFilter radarFilter = new RadarFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title, asc"));
    Page<RadarDto> radarDtoPage = radarService.findAll(radarFilter, pageable);
    Assertions.assertEquals(1, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(radarDtoPage.iterator().next().getId(), radar.getId());
    Assertions.assertEquals(radarDtoPage.iterator().next().getTitle(), radar.getTitle());
    Assertions.assertEquals(radarDtoPage.iterator().next().getDescription(), radar.getDescription());

    // Mockito.verify(radarRepository).findAll(Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  /* TODO


  @Test
  @Transactional
  void shouldFindAllRadarsWithBlankTitleFilter() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("My radar type title");
    radarType.setDescription("My radar type description");
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarTypeRepository.saveAndFlush(radarType);

    List<Radar> radarList = List.of(
        new Radar(null, radarType, "My first radar title", "Description", false, false),
        new Radar(null, radarType, "My second radar title", "New description", false, false)
    );
    for (Radar radar : radarList) {
      radarRepository.save(radar);
    }

    RadarFilter radarFilter = new RadarFilter();
    radarFilter.setTitle("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarDto> radarDtoPage = radarService.findAll(radarFilter, pageable);
    Assertions.assertEquals(10, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(2, radarDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllRadarsWithTitleFilter() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("My radar type title");
    radarType.setDescription("My radar type description");
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarTypeRepository.saveAndFlush(radarType);

    List<Radar> radarList = List.of(
        new Radar(null, radarType, "My first radar title", "Description", false, false),
        new Radar(null, radarType, "My second radar title", "New description", false, false)
    );    for (Radar radar : radarList) {
      radarRepository.save(radar);
    }

    RadarFilter radarFilter = new RadarFilter();
    radarFilter.setTitle(radarList.iterator().next().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarDto> radarDtoPage = radarService.findAll(radarFilter, pageable);
    Assertions.assertEquals(10, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(1, radarDtoPage.getNumberOfElements());
    Assertions.assertNotNull(radarDtoPage.iterator().next().getId());
    Assertions.assertEquals(radarDtoPage.iterator().next().getTitle(), radarList.iterator().next().getTitle());
    Assertions.assertEquals(radarDtoPage.iterator().next().getDescription(),
        radarList.iterator().next().getDescription());
  }

  @Test
  @Transactional
  void shouldFindAllRadarsWithPrimaryFilter() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("My radar title");
    radarType.setDescription("My radar type description");
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarTypeRepository.saveAndFlush(radarType);

    List<Radar> radarList = List.of(
        new Radar(null, radarType, "My first radar title", "Description", true, false),
        new Radar(null, radarType, "My second radar title", "New description", false, false)
    );    for (Radar radar : radarList) {
      radarRepository.save(radar);
    }

    RadarFilter radarFilter = new RadarFilter();
    radarFilter.setFilterByPrimary(true);
    radarFilter.setPrimary(true);
    radarFilter.setFilterByActive(true);
    radarFilter.setActive(false);
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarDto> radarDtoPage = radarService.findAll(radarFilter, pageable);
    Assertions.assertEquals(10, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(1, radarDtoPage.getNumberOfElements());
    Assertions.assertNotNull(radarDtoPage.iterator().next().getId());
    Assertions.assertEquals(radarDtoPage.iterator().next().getTitle(), radarList.iterator().next().getTitle());
    Assertions.assertEquals(radarDtoPage.iterator().next().getDescription(),
        radarList.iterator().next().getDescription());
    Assertions.assertTrue(radarDtoPage.iterator().next().isPrimary());
  }

  @Test
  @Transactional
  void shouldFindAllRadarsWithActiveFilter() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("My radar type title");
    radarType.setDescription("My radar type description");
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarTypeRepository.saveAndFlush(radarType);

    List<Radar> radarList = List.of(
        new Radar(null, radarType, "My first radar title", "Description", false, true),
        new Radar(null, radarType, "My second radar title", "New description", true, false)
    );    for (Radar radar : radarList) {
      radarRepository.save(radar);
    }

    RadarFilter radarFilter = new RadarFilter();
    radarFilter.setFilterByPrimary(true);
    radarFilter.setPrimary(false);
    radarFilter.setFilterByActive(true);
    radarFilter.setActive(true);
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarDto> radarDtoPage = radarService.findAll(radarFilter, pageable);
    Assertions.assertEquals(10, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(1, radarDtoPage.getNumberOfElements());
    Assertions.assertNotNull(radarDtoPage.iterator().next().getId());
    Assertions.assertEquals(radarDtoPage.iterator().next().getTitle(), radarList.iterator().next().getTitle());
    Assertions.assertEquals(radarDtoPage.iterator().next().getDescription(),
        radarList.iterator().next().getDescription());
    Assertions.assertTrue(radarDtoPage.iterator().next().isActive());
  }

  @Test
  @Transactional
  void shouldFindAllRadarsWithPrimaryAndActiveFilter() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("My radar type title");
    radarType.setDescription("My radar type description");
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarTypeRepository.saveAndFlush(radarType);

    List<Radar> radarList = List.of(
        new Radar(null, radarType, "My first radar title", "Description", true, true),
        new Radar(null, radarType, "My second radar title", "New description", false, false)
    );    for (Radar radar : radarList) {
      radarRepository.save(radar);
    }

    RadarFilter radarFilter = new RadarFilter();
    radarFilter.setFilterByPrimary(true);
    radarFilter.setPrimary(true);
    radarFilter.setFilterByActive(true);
    radarFilter.setActive(true);
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarDto> radarDtoPage = radarService.findAll(radarFilter, pageable);
    Assertions.assertEquals(10, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(1, radarDtoPage.getNumberOfElements());
    Assertions.assertNotNull(radarDtoPage.iterator().next().getId());
    Assertions.assertEquals(radarDtoPage.iterator().next().getTitle(), radarList.iterator().next().getTitle());
    Assertions.assertEquals(radarDtoPage.iterator().next().getDescription(),
        radarList.iterator().next().getDescription());
    Assertions.assertTrue(radarDtoPage.iterator().next().isActive());
    Assertions.assertTrue(radarDtoPage.iterator().next().isPrimary());
  }
   */

  @Test
  void shouldFindAllRadarsWithFullFilter() {
    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar Description");
    radar.setPrimary(true);
    radar.setActive(true);

    List<Radar> radarList = List.of(radar);
    Page<Radar> page = new PageImpl<>(radarList);
    Mockito.when(radarRepository.findAll(ArgumentMatchers.<Specification<Radar>>any(), any(Pageable.class)))
        .thenReturn(page);

    RadarFilter radarFilter = new RadarFilter();
    radarFilter.setTitle(radar.getTitle());
    radarFilter.setFilterByPrimary(true);
    radarFilter.setPrimary(true);
    radarFilter.setFilterByActive(true);
    radarFilter.setActive(true);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title, asc"));
    Page<RadarDto> radarDtoPage = radarService.findAll(radarFilter, pageable);
    Assertions.assertEquals(1, radarDtoPage.getSize());
    Assertions.assertEquals(0, radarDtoPage.getNumber());
    Assertions.assertEquals(1, radarDtoPage.getTotalPages());
    Assertions.assertEquals(radarDtoPage.iterator().next().getId(), radar.getId());
    Assertions.assertEquals(radarDtoPage.iterator().next().getTitle(), radar.getTitle());
    Assertions.assertEquals(radarDtoPage.iterator().next().getDescription(), radar.getDescription());

    // Mockito.verify(radarRepository).findAll(Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindByIdRadar() {
    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar Description");

    Mockito.when(radarRepository.findById(radar.getId())).thenReturn(Optional.of(radar));

    Optional<RadarDto> radarDtoOptional = radarService.findById(radar.getId());
    Assertions.assertTrue(radarDtoOptional.isPresent());
    Assertions.assertEquals(radar.getId(), radarDtoOptional.get().getId());
    Assertions.assertEquals(radar.getTitle(), radarDtoOptional.get().getTitle());
    Assertions.assertEquals(radar.getDescription(), radarDtoOptional.get().getDescription());

    Mockito.verify(radarRepository).findById(radar.getId());
  }

  @Test
  void shouldSaveRadarDto() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    Mockito.when(radarRepository.save(any())).thenReturn(radar);
    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(new LinkedList<>());
    Mockito.when(radarRepository.findByTitle(radar.getTitle())).thenReturn(new LinkedList<>());
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    RadarDto radarDto = radarService.save(radarMapper.toDto(radar));
    Assertions.assertEquals(radar.getId(), radarDto.getId());
    Assertions.assertEquals(radar.getTitle(), radarDto.getTitle());
    Assertions.assertEquals(radar.getDescription(), radarDto.getDescription());

    Mockito.verify(radarRepository).save(any());
    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldFailToTheSaveSecondPrimaryRadarDtoAndEmptyTitle() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle("");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    final Radar radar1 = new Radar();
    radar1.setId(12L);
    radar1.setRadarType(radarType);
    radar1.setTitle("Any radar title");
    radar1.setDescription("Radar description");
    radar1.setPrimary(true);
    radar1.setActive(false);
    List<Radar> radarList = List.of(radar1);

    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(radarList);
    Mockito.when(radarRepository.findByTitle(radar1.getTitle())).thenReturn(radarList);
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    ValidationException exception =
        catchThrowableOfType(() -> radarService.save(radarMapper.toDto(radar)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("must not be blank"));
    Assertions.assertTrue(exception.getMessage().contains("should be only one primary radar"));
    Assertions.assertTrue(exception.getMessage().contains("size must be between 1 and 64"));

    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldFailToSaveRadarDtoDueToTitleWithWhiteSpace() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle(" Radar title ");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(new LinkedList<>());
    Mockito.when(radarRepository.findByTitle(radar.getTitle())).thenReturn(new LinkedList<>());
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    ValidationException exception =
        catchThrowableOfType(() -> radarService.save(radarMapper.toDto(radar)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));

    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldSaveTheActiveRadarDto() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Ring ring0 = new Ring();
    final Ring ring1 = new Ring();
    final Ring ring2 = new Ring();
    final Ring ring3 = new Ring();
    ring0.setPosition(0);
    ring1.setPosition(1);
    ring2.setPosition(2);
    ring3.setPosition(3);

    final Segment segment0 = new Segment();
    final Segment segment1 = new Segment();
    final Segment segment2 = new Segment();
    final Segment segment3 = new Segment();
    segment0.setPosition(0);
    segment1.setPosition(1);
    segment2.setPosition(2);
    segment3.setPosition(3);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(true);
    radar.setSegmentList(List.of(segment0, segment1, segment2, segment3));
    radar.setRingList(List.of(ring0, ring1, ring2, ring3));
    Assertions.assertEquals(radar.getRingList().size(), 4);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(radarRepository.save(any())).thenReturn(radar);
    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(new LinkedList<>());
    Mockito.when(radarRepository.findByTitle(radar.getTitle())).thenReturn(new LinkedList<>());
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    RadarDto radarDto = radarService.save(radarMapper.toDto(radar));
    Assertions.assertEquals(radar.getId(), radarDto.getId());
    Assertions.assertEquals(radar.getTitle(), radarDto.getTitle());
    Assertions.assertEquals(radar.getDescription(), radarDto.getDescription());
    Assertions.assertEquals(radar.getRingList().size(), radarDto.getRingDtoList().size());
    Assertions.assertEquals(radar.getSegmentList().size(), radarDto.getSegmentDtoList().size());

    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(radarRepository).save(any());
    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldFailToSaveActiveRadarDtoDueToMinimumRingAndSegment() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(true);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(new LinkedList<>());
    Mockito.when(radarRepository.findByTitle(radar.getTitle())).thenReturn(new LinkedList<>());
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    ValidationException exception =
        catchThrowableOfType(() -> radarService.save(radarMapper.toDto(radar)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertNull(radar.getSegmentList());
    Assertions.assertNull(radar.getRingList());

    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldFailToSaveActiveRadarDtoDueToWrongRingAndSegmentPosition() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Ring ring0 = new Ring();
    final Ring ring1 = new Ring();
    final Ring ring2 = new Ring();
    final Ring ring3 = new Ring();
    ring0.setPosition(0);
    ring1.setPosition(0);
    ring2.setPosition(4);
    ring3.setPosition(5);

    final Segment segment0 = new Segment();
    final Segment segment1 = new Segment();
    final Segment segment2 = new Segment();
    final Segment segment3 = new Segment();
    segment0.setPosition(0);
    segment1.setPosition(0);
    segment2.setPosition(4);
    segment3.setPosition(5);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(true);
    radar.setSegmentList(List.of(segment0, segment1, segment2, segment3));
    radar.setRingList(List.of(ring0, ring1, ring2, ring3));

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(new LinkedList<>());
    Mockito.when(radarRepository.findByTitle(radar.getTitle())).thenReturn(new LinkedList<>());
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    ValidationException exception =
        catchThrowableOfType(() -> radarService.save(radarMapper.toDto(radar)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertEquals(radar.getSegmentList().size(), 4);
    Assertions.assertEquals(radar.getRingList().size(), 4);

    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldFailToTheSaveSecondPrimaryRadarDto() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    final Radar radar1 = new Radar();
    radar1.setId(12L);
    radar1.setRadarType(radarType);
    radar1.setTitle("Any radar title");
    radar1.setDescription("Radar description");
    radar1.setPrimary(true);
    radar1.setActive(false);
    List<Radar> radarList = List.of(radar1);

    Mockito.when(radarRepository.save(any())).thenReturn(radar);
    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(radarList);
    Mockito.when(radarRepository.findByTitle(radar.getTitle())).thenReturn(radarList);
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    ValidationException exception =
        catchThrowableOfType(() -> radarService.save(radarMapper.toDto(radar)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());

    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldFailToTheSaveSecondUniqueTitleRadarDto() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(radarType);
    radar.setTitle("Radar title");
    radar.setPrimary(true);
    radar.setActive(true);
    radar.setDescription("Radar description");

    final Radar radar1 = new Radar();
    radar1.setId(12L);
    radar1.setRadarType(radarType);
    radar1.setTitle("Radar title");
    radar1.setPrimary(false);
    radar1.setActive(true);
    radar1.setDescription("Radar description");
    List<Radar> radarList = List.of(radar1);

    Mockito.when(radarRepository.save(any())).thenReturn(radar);
    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(radarList);
    Mockito.when(radarRepository.findByTitle(any())).thenReturn(radarList);
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    ValidationException exception =
        catchThrowableOfType(() -> radarService.save(radarMapper.toDto(radar)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());

    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldSucceedToSavePrimaryRadarDtoWithNonPrimaryRadar() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);

    final Radar radar = new Radar();
    radar.setId(2L);
    radar.setRadarType(radarType);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    final Radar radar1 = new Radar();
    radar1.setId(3L);
    radar1.setRadarType(radarType);
    radar1.setTitle("Any radar title");
    radar1.setDescription("Radar description");
    radar1.setPrimary(false);
    radar1.setActive(false);
    List<Radar> radarList = List.of(radar1);

    Mockito.when(radarRepository.save(any())).thenReturn(radar);
    Mockito.when(radarRepository.findByPrimary(anyBoolean())).thenReturn(radarList);
    Mockito.when(radarRepository.findByTitle(radar1.getTitle())).thenReturn(radarList);
    Mockito.when(radarTypeRepository.findById(any())).thenReturn(Optional.of(radarType));

    RadarDto radarDto = radarService.save(radarMapper.toDto(radar));
    Assertions.assertEquals(radar.getId(), radarDto.getId());
    Assertions.assertEquals(radar.getTitle(), radarDto.getTitle());
    Assertions.assertEquals(radar.getDescription(), radarDto.getDescription());

    Mockito.verify(radarRepository).save(any());
    Mockito.verify(radarRepository).findByPrimary(true);
    Mockito.verify(radarRepository).findByTitle(any());
    Mockito.verify(radarTypeRepository).findById(radarType.getId());
  }

  @Test
  void shouldDeleteRadar() {
    final Radar radar = new Radar();
    radar.setId(10L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar Description");

    Mockito.doAnswer((i) -> null).when(radarRepository).deleteById(radar.getId());

    radarService.deleteById(radar.getId());
    Mockito.verify(radarRepository).deleteById(radar.getId());
  }
}


