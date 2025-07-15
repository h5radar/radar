package com.h5radar.radar.domain.maturity;

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

class MaturityServiceTests extends AbstractServiceTests {

  @MockitoBean
  private MaturityRepository maturityRepository;
  @MockitoBean
  private RadarRepository radarRepository;
  @Autowired
  private MaturityMapper maturityMapper;
  @Autowired
  private MaturityService maturityService;

  @Test
  void shouldFindAllMaturitys() {
    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setTitle("My title");
    maturity.setDescription("My description");
    maturity.setColor("My color");
    maturity.setPosition(1);

    List<Maturity> maturityList = List.of(maturity);
    Mockito.when(maturityRepository.findAll(any(Sort.class))).thenReturn(maturityList);

    Collection<MaturityDto> maturityDtoCollection = maturityService.findAll();
    Assertions.assertEquals(1, maturityDtoCollection.size());
    Assertions.assertEquals(maturityDtoCollection.iterator().next().getId(), maturity.getId());
    Assertions.assertEquals(maturityDtoCollection.iterator().next().getTitle(), maturity.getTitle());
    Assertions.assertEquals(maturityDtoCollection.iterator().next().getDescription(), maturity.getDescription());
    Assertions.assertEquals(maturityDtoCollection.iterator().next().getColor(), maturity.getColor());
    Assertions.assertEquals(maturityDtoCollection.iterator().next().getPosition(), maturity.getPosition());

  }

  @Test
  void shouldFindAllMaturitysWithNullFilter() {
    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setTitle("My title");
    maturity.setDescription("My description");
    maturity.setColor("My color");
    maturity.setPosition(1);

    List<Maturity> maturityList = List.of(maturity);
    Page<Maturity> page = new PageImpl<>(maturityList);
    Mockito.when(maturityRepository.findAll(ArgumentMatchers.<Specification<Maturity>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<MaturityDto> maturityDtoPage = maturityService.findAll(null, pageable);
    Assertions.assertEquals(1, maturityDtoPage.getSize());
    Assertions.assertEquals(0, maturityDtoPage.getNumber());
    Assertions.assertEquals(1, maturityDtoPage.getTotalPages());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getId(), maturity.getId());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getTitle(), maturity.getTitle());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getDescription(), maturity.getDescription());

    // Mockito.verify(tenantRepository).findAll(Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindAllMaturitysWithEmptyFilter() {
    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setTitle("My title");
    maturity.setDescription("My description");
    maturity.setColor("My color");
    maturity.setPosition(1);

    List<Maturity> maturityList = List.of(maturity);
    Page<Maturity> page = new PageImpl<>(maturityList);
    Mockito.when(maturityRepository.findAll(ArgumentMatchers.<Specification<Maturity>>any(), any(Pageable.class)))
        .thenReturn(page);

    MaturityFilter maturityFilter = new MaturityFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<MaturityDto> maturityDtoPage = maturityService.findAll(maturityFilter, pageable);
    Assertions.assertEquals(1, maturityDtoPage.getSize());
    Assertions.assertEquals(0, maturityDtoPage.getNumber());
    Assertions.assertEquals(1, maturityDtoPage.getTotalPages());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getId(), maturity.getId());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getTitle(), maturity.getTitle());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getDescription(), maturity.getDescription());

    // Mockito.verify(tenantRepository).findAll(Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  /* TODO:

  @Test
  @Transactional
  void shouldFindAllMaturitysWithBlankTitleFilter() {
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

    List<Maturity> maturityList = List.of(
        new Maturity(null, radar, "TRIAL", "Description", 0, "Color", null),
        new Maturity(null, radar, "ADOPT", "New description", 1, "Color", null)
    );
    for (Maturity maturity : maturityList) {
      maturityRepository.save(maturity);
    }

    MaturityFilter maturityFilter = new MaturityFilter();
    maturityFilter.setTitle("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<MaturityDto> maturityDtoPage = maturityService.findAll(maturityFilter, pageable);
    Assertions.assertEquals(10, maturityDtoPage.getSize());
    Assertions.assertEquals(0, maturityDtoPage.getNumber());
    Assertions.assertEquals(1, maturityDtoPage.getTotalPages());
    Assertions.assertEquals(2, maturityDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllMaturitysWithTitleFilter() {
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

    List<Maturity> maturityList = List.of(
        new Maturity(null, radar, "TRIAL", "Description", 0, "Color", null),
        new Maturity(null, radar, "ADOPT", "New description", 1, "Color", null)
    );
    for (Maturity maturity : maturityList) {
      maturityRepository.save(maturity);
    }

    MaturityFilter maturityFilter = new MaturityFilter();
    maturityFilter.setTitle(maturityList.iterator().next().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<MaturityDto> maturityDtoPage = maturityService.findAll(maturityFilter, pageable);
    Assertions.assertEquals(10, maturityDtoPage.getSize());
    Assertions.assertEquals(0, maturityDtoPage.getNumber());
    Assertions.assertEquals(1, maturityDtoPage.getTotalPages());
    Assertions.assertEquals(1, maturityDtoPage.getNumberOfElements());
    Assertions.assertNotNull(maturityDtoPage.iterator().next().getId());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getTitle(), maturityList.iterator().next().getTitle());
    Assertions.assertEquals(maturityDtoPage.iterator().next().getDescription(),
        maturityList.iterator().next().getDescription());
  }
   */

  @Test
  void shouldFindByIdMaturity() {
    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setTitle("My title");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(maturityRepository.findById(maturity.getId())).thenReturn(Optional.of(maturity));

    Optional<MaturityDto> maturityDtoOptional = maturityService.findById(maturity.getId());
    Assertions.assertTrue(maturityDtoOptional.isPresent());
    Assertions.assertEquals(maturity.getId(), maturityDtoOptional.get().getId());
    Assertions.assertEquals(maturity.getTitle(), maturityDtoOptional.get().getTitle());
    Assertions.assertEquals(maturity.getDescription(), maturityDtoOptional.get().getDescription());

    Mockito.verify(maturityRepository).findById(maturity.getId());
  }

  @Test
  void shouldFindByTitleMaturity() {
    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setTitle("My title");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(maturityRepository.findByTitle(maturity.getTitle())).thenReturn(Optional.of(maturity));

    Optional<MaturityDto> maturityDtoOptional = maturityService.findByTitle(maturity.getTitle());
    Assertions.assertTrue(maturityDtoOptional.isPresent());
    Assertions.assertEquals(maturity.getId(), maturityDtoOptional.get().getId());
    Assertions.assertEquals(maturity.getTitle(), maturityDtoOptional.get().getTitle());
    Assertions.assertEquals(maturity.getDescription(), maturityDtoOptional.get().getDescription());
    Assertions.assertEquals(maturity.getColor(), maturityDtoOptional.get().getColor());
    Assertions.assertEquals(maturity.getPosition(), maturityDtoOptional.get().getPosition());

    Mockito.verify(maturityRepository).findByTitle(maturity.getTitle());
  }

  @Test
  void shouldSaveMaturity() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setRadar(radar);
    maturity.setTitle("ADOPT");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(maturityRepository.save(any())).thenReturn(maturity);

    MaturityDto maturityDto = maturityService.save(maturityMapper.toDto(maturity));
    Assertions.assertEquals(maturity.getId(), maturityDto.getId());
    Assertions.assertEquals(maturity.getTitle(), maturityDto.getTitle());
    Assertions.assertEquals(maturity.getDescription(), maturityDto.getDescription());

    Mockito.verify(maturityRepository).save(any());
    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
  }

  @Test
  void shouldFailToSaveMaturityDueToTitleWithWhiteSpace() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setRadar(radar);
    maturity.setTitle(" ADOPT ");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    ValidationException exception =
        catchThrowableOfType(() -> maturityService.save(maturityMapper.toDto(maturity)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
  }

  @Test
  void shouldFailToSaveMaturityDueToRadarIsActive() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setRadar(radar);
    maturity.setTitle("ADOPT");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));
    Mockito.when(maturityRepository.findById(any())).thenReturn(Optional.of(maturity));

    ValidationException exception =
        catchThrowableOfType(() -> maturityService.save(maturityMapper.toDto(maturity)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
    Mockito.verify(maturityRepository).findById(maturity.getId());
  }

  @Test
  void shouldFailToSaveMaturityDueToBelongActiveRadar() {
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

    final Maturity maturity = new Maturity();
    maturity.setId(3L);
    maturity.setRadar(radarActive);
    maturity.setTitle("ADOPT");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(radarRepository.findById(maturity.getRadar().getId())).thenReturn(Optional.of(radar));
    Mockito.when(maturityRepository.findById(maturity.getId())).thenReturn(Optional.of(maturity));

    ValidationException exception =
            catchThrowableOfType(() -> maturityService.save(maturityMapper.toDto(maturity)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("can't be saved for active radar"));

    Mockito.verify(radarRepository, Mockito.times(2)).findById(radarActive.getId());
  }

  @Test
  void shouldDeleteMaturity() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setRadar(radar);
    maturity.setTitle("ADOPT");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(maturityRepository.findById(any())).thenReturn(Optional.of(maturity));
    Mockito.doAnswer((i) -> null).when(maturityRepository).deleteById(maturity.getId());

    maturityService.deleteById(maturity.getId());
    Mockito.verify(maturityRepository).findById(maturity.getId());
    Mockito.verify(maturityRepository).deleteById(maturity.getId());
  }

  @Test
  void shouldFailToDeleteMaturityDueToRadarIsActive() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Maturity maturity = new Maturity();
    maturity.setId(10L);
    maturity.setRadar(radar);
    maturity.setTitle("ADOPT");
    maturity.setDescription("My description");
    maturity.setColor("my color");
    maturity.setPosition(1);

    Mockito.when(maturityRepository.findById(any())).thenReturn(Optional.of(maturity));

    ValidationException exception =
        catchThrowableOfType(() -> maturityService.deleteById(maturity.getId()), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertEquals(exception.getMessage(), "Maturity can't be deleted for active radar.");
    Assertions.assertTrue(maturity.getId().describeConstable().isPresent());

    Mockito.verify(maturityRepository).findById(maturity.getId());
  }
}
