package com.h5radar.radar.domain.ring;

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

class RingServiceTests extends AbstractServiceTests {

  @MockitoBean
  private RingRepository ringRepository;
  @MockitoBean
  private RadarRepository radarRepository;
  @Autowired
  private RingMapper ringMapper;
  @Autowired
  private RingService ringService;

  @Test
  void shouldFindAllRings() {
    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setTitle("My title");
    ring.setDescription("My description");
    ring.setColor("My color");
    ring.setPosition(1);

    List<Ring> ringList = List.of(ring);
    Mockito.when(ringRepository.findAll(any(Sort.class))).thenReturn(ringList);

    Collection<RingDto> ringDtoCollection = ringService.findAll();
    Assertions.assertEquals(1, ringDtoCollection.size());
    Assertions.assertEquals(ringDtoCollection.iterator().next().getId(), ring.getId());
    Assertions.assertEquals(ringDtoCollection.iterator().next().getTitle(), ring.getTitle());
    Assertions.assertEquals(ringDtoCollection.iterator().next().getDescription(), ring.getDescription());
    Assertions.assertEquals(ringDtoCollection.iterator().next().getColor(), ring.getColor());
    Assertions.assertEquals(ringDtoCollection.iterator().next().getPosition(), ring.getPosition());

  }

  @Test
  void shouldFindAllRingsWithNullFilter() {
    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setTitle("My title");
    ring.setDescription("My description");
    ring.setColor("My color");
    ring.setPosition(1);

    List<Ring> ringList = List.of(ring);
    Page<Ring> page = new PageImpl<>(ringList);
    Mockito.when(ringRepository.findAll(ArgumentMatchers.<Specification<Ring>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<RingDto> ringDtoPage = ringService.findAll(null, pageable);
    Assertions.assertEquals(1, ringDtoPage.getSize());
    Assertions.assertEquals(0, ringDtoPage.getNumber());
    Assertions.assertEquals(1, ringDtoPage.getTotalPages());
    Assertions.assertEquals(ringDtoPage.iterator().next().getId(), ring.getId());
    Assertions.assertEquals(ringDtoPage.iterator().next().getTitle(), ring.getTitle());
    Assertions.assertEquals(ringDtoPage.iterator().next().getDescription(), ring.getDescription());

    // Mockito.verify(tenantRepository).findAll(Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindAllRingsWithEmptyFilter() {
    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setTitle("My title");
    ring.setDescription("My description");
    ring.setColor("My color");
    ring.setPosition(1);

    List<Ring> ringList = List.of(ring);
    Page<Ring> page = new PageImpl<>(ringList);
    Mockito.when(ringRepository.findAll(ArgumentMatchers.<Specification<Ring>>any(), any(Pageable.class)))
        .thenReturn(page);

    RingFilter ringFilter = new RingFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<RingDto> ringDtoPage = ringService.findAll(ringFilter, pageable);
    Assertions.assertEquals(1, ringDtoPage.getSize());
    Assertions.assertEquals(0, ringDtoPage.getNumber());
    Assertions.assertEquals(1, ringDtoPage.getTotalPages());
    Assertions.assertEquals(ringDtoPage.iterator().next().getId(), ring.getId());
    Assertions.assertEquals(ringDtoPage.iterator().next().getTitle(), ring.getTitle());
    Assertions.assertEquals(ringDtoPage.iterator().next().getDescription(), ring.getDescription());

    // Mockito.verify(tenantRepository).findAll(Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  /* TODO:

  @Test
  @Transactional
  void shouldFindAllRingsWithBlankTitleFilter() {
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
    radarRepository.saveAndFlush(radar);

    List<Ring> ringList = List.of(
        new Ring(null, radar, "TRIAL", "Description", 0, "Color", null),
        new Ring(null, radar, "ADOPT", "New description", 1, "Color", null)
    );
    for (Ring ring : ringList) {
      ringRepository.save(ring);
    }

    RingFilter ringFilter = new RingFilter();
    ringFilter.setTitle("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RingDto> ringDtoPage = ringService.findAll(ringFilter, pageable);
    Assertions.assertEquals(10, ringDtoPage.getSize());
    Assertions.assertEquals(0, ringDtoPage.getNumber());
    Assertions.assertEquals(1, ringDtoPage.getTotalPages());
    Assertions.assertEquals(2, ringDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllRingsWithTitleFilter() {
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
    radarRepository.saveAndFlush(radar);

    List<Ring> ringList = List.of(
        new Ring(null, radar, "TRIAL", "Description", 0, "Color", null),
        new Ring(null, radar, "ADOPT", "New description", 1, "Color", null)
    );
    for (Ring ring : ringList) {
      ringRepository.save(ring);
    }

    RingFilter ringFilter = new RingFilter();
    ringFilter.setTitle(ringList.iterator().next().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RingDto> ringDtoPage = ringService.findAll(ringFilter, pageable);
    Assertions.assertEquals(10, ringDtoPage.getSize());
    Assertions.assertEquals(0, ringDtoPage.getNumber());
    Assertions.assertEquals(1, ringDtoPage.getTotalPages());
    Assertions.assertEquals(1, ringDtoPage.getNumberOfElements());
    Assertions.assertNotNull(ringDtoPage.iterator().next().getId());
    Assertions.assertEquals(ringDtoPage.iterator().next().getTitle(), ringList.iterator().next().getTitle());
    Assertions.assertEquals(ringDtoPage.iterator().next().getDescription(),
        ringList.iterator().next().getDescription());
  }
   */

  @Test
  void shouldFindByIdRing() {
    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setTitle("My title");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(ringRepository.findById(ring.getId())).thenReturn(Optional.of(ring));

    Optional<RingDto> ringDtoOptional = ringService.findById(ring.getId());
    Assertions.assertTrue(ringDtoOptional.isPresent());
    Assertions.assertEquals(ring.getId(), ringDtoOptional.get().getId());
    Assertions.assertEquals(ring.getTitle(), ringDtoOptional.get().getTitle());
    Assertions.assertEquals(ring.getDescription(), ringDtoOptional.get().getDescription());

    Mockito.verify(ringRepository).findById(ring.getId());
  }

  @Test
  void shouldFindByTitleRing() {
    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setTitle("My title");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(ringRepository.findByTitle(ring.getTitle())).thenReturn(Optional.of(ring));

    Optional<RingDto> ringDtoOptional = ringService.findByTitle(ring.getTitle());
    Assertions.assertTrue(ringDtoOptional.isPresent());
    Assertions.assertEquals(ring.getId(), ringDtoOptional.get().getId());
    Assertions.assertEquals(ring.getTitle(), ringDtoOptional.get().getTitle());
    Assertions.assertEquals(ring.getDescription(), ringDtoOptional.get().getDescription());
    Assertions.assertEquals(ring.getColor(), ringDtoOptional.get().getColor());
    Assertions.assertEquals(ring.getPosition(), ringDtoOptional.get().getPosition());

    Mockito.verify(ringRepository).findByTitle(ring.getTitle());
  }

  @Test
  void shouldSaveRing() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setRadar(radar);
    ring.setTitle("ADOPT");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(ringRepository.save(any())).thenReturn(ring);

    RingDto ringDto = ringService.save(ringMapper.toDto(ring));
    Assertions.assertEquals(ring.getId(), ringDto.getId());
    Assertions.assertEquals(ring.getTitle(), ringDto.getTitle());
    Assertions.assertEquals(ring.getDescription(), ringDto.getDescription());

    Mockito.verify(ringRepository).save(any());
    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
  }

  @Test
  void shouldFailToSaveRingDueToTitleWithWhiteSpace() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setRadar(radar);
    ring.setTitle(" ADOPT ");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    ValidationException exception =
        catchThrowableOfType(() -> ringService.save(ringMapper.toDto(ring)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
  }

  @Test
  void shouldFailToSaveRingDueToRadarIsActive() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setRadar(radar);
    ring.setTitle("ADOPT");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(ringRepository.findById(any())).thenReturn(Optional.of(ring));

    ValidationException exception =
        catchThrowableOfType(() -> ringService.save(ringMapper.toDto(ring)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
    Mockito.verify(ringRepository).findById(ring.getId());
  }

  @Test
  void shouldFailToSaveRingDueToBelongActiveRadar() {
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

    final Ring ring = new Ring();
    ring.setId(3L);
    ring.setRadar(radarActive);
    ring.setTitle("ADOPT");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(radarRepository.findById(ring.getRadar().getId())).thenReturn(Optional.of(radar));
    Mockito.when(ringRepository.findById(ring.getId())).thenReturn(Optional.of(ring));

    ValidationException exception =
            catchThrowableOfType(() -> ringService.save(ringMapper.toDto(ring)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("can't be saved for active radar"));

    Mockito.verify(radarRepository, Mockito.times(2)).findById(radarActive.getId());
  }

  @Test
  void shouldDeleteRing() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setRadar(radar);
    ring.setTitle("ADOPT");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(ringRepository.findById(any())).thenReturn(Optional.of(ring));
    Mockito.doAnswer((i) -> null).when(ringRepository).deleteById(ring.getId());

    ringService.deleteById(ring.getId());
    Mockito.verify(ringRepository).findById(ring.getId());
    Mockito.verify(ringRepository).deleteById(ring.getId());
  }

  @Test
  void shouldFailToDeleteRingDueToRadarIsActive() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Ring ring = new Ring();
    ring.setId(10L);
    ring.setRadar(radar);
    ring.setTitle("ADOPT");
    ring.setDescription("My description");
    ring.setColor("my color");
    ring.setPosition(1);

    Mockito.when(ringRepository.findById(any())).thenReturn(Optional.of(ring));

    ValidationException exception =
        catchThrowableOfType(() -> ringService.deleteById(ring.getId()), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertEquals(exception.getMessage(), "Ring can't be deleted for active radar.");
    Assertions.assertTrue(ring.getId().describeConstable().isPresent());

    Mockito.verify(ringRepository).findById(ring.getId());
  }
}
