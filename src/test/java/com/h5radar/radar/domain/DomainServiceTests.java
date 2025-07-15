package com.h5radar.radar.domain.domain;

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

class DomainServiceTests extends AbstractServiceTests {
  @MockitoBean
  private DomainRepository domainRepository;

  // TODO: @MockitoBean
  // private RadarRepository radarRepository;

  @Autowired
  private DomainMapper domainMapper;

  @Autowired
  private DomainService domainService;

  @Test
  void shouldFindAllDomains() {
    final Domain domain = new Domain();
    domain.setId(10L);
    domain.setRadarUser(null);
    domain.setTitle("My domain");
    domain.setDescription("My domain description");
    domain.setPosition(1);

    List<Domain> domainList = List.of(domain);
    Mockito.when(domainRepository.findAll(any(Sort.class))).thenReturn(domainList);

    Collection<DomainDto> domainDtoCollection = domainService.findAll();
    Assertions.assertEquals(1, domainDtoCollection.size());
    Assertions.assertEquals(domainDtoCollection.iterator().next().getId(), domain.getId());
    Assertions.assertEquals(domainDtoCollection.iterator().next().getTitle(), domain.getTitle());
    Assertions.assertEquals(domainDtoCollection.iterator().next().getDescription(), domain.getDescription());
  }

  @Test
  void shouldFindAllDomainsWithNullFilter() {
    final Domain domain = new Domain();
    domain.setId(10L);
    domain.setRadarUser(null);
    domain.setTitle("My domain");
    domain.setDescription("My domain description");
    domain.setPosition(1);

    List<Domain> domainList = List.of(domain);
    Page<Domain> page = new PageImpl<>(domainList);
    Mockito.when(domainRepository.findAll(ArgumentMatchers.<Specification<Domain>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<DomainDto> domainDtoPage = domainService.findAll(null, pageable);
    Assertions.assertEquals(1, domainDtoPage.getSize());
    Assertions.assertEquals(0, domainDtoPage.getNumber());
    Assertions.assertEquals(1, domainDtoPage.getTotalPages());
    Assertions.assertEquals(domainDtoPage.iterator().next().getId(), domain.getId());
    Assertions.assertEquals(domainDtoPage.iterator().next().getTitle(), domain.getTitle());
    Assertions.assertEquals(domainDtoPage.iterator().next().getDescription(), domain.getDescription());

    // Mockito.verify(domainRepository).findAll(
    //    Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindAllDomainsWithEmptyFilter() {
    final Domain domain = new Domain();
    domain.setId(10L);
    domain.setRadarUser(null);
    domain.setTitle("My domain");
    domain.setDescription("My domain description");
    domain.setPosition(1);

    List<Domain> domainList = List.of(domain);
    Page<Domain> page = new PageImpl<>(domainList);
    Mockito.when(domainRepository.findAll(ArgumentMatchers.<Specification<Domain>>any(), any(Pageable.class)))
        .thenReturn(page);

    DomainFilter domainFilter = new DomainFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<DomainDto> domainDtoPage = domainService.findAll(domainFilter, pageable);
    Assertions.assertEquals(1, domainDtoPage.getSize());
    Assertions.assertEquals(0, domainDtoPage.getNumber());
    Assertions.assertEquals(1, domainDtoPage.getTotalPages());
    Assertions.assertEquals(domainDtoPage.iterator().next().getId(), domain.getId());
    Assertions.assertEquals(domainDtoPage.iterator().next().getTitle(), domain.getTitle());
    Assertions.assertEquals(domainDtoPage.iterator().next().getDescription(), domain.getDescription());

    // Mockito.verify(domainRepository).findAll(
    //    Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  /* TODO

  @Test
  @Transactional
  void shouldFindAllDomainsWithBlankTitleFilter() {
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

    List<Domain> domainList = List.of(
        new Domain(null, radar, "My first domain title", "Description", 0, null),
        new Domain(null, radar, "My second domain title", "New description", 1, null)
    );
    for (Domain domain : domainList) {
      domainRepository.save(domain);
    }

    DomainFilter domainFilter = new DomainFilter();
    domainFilter.setTitle("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<DomainDto> domainDtoPage = domainService.findAll(domainFilter, pageable);
    Assertions.assertEquals(10, domainDtoPage.getSize());
    Assertions.assertEquals(0, domainDtoPage.getNumber());
    Assertions.assertEquals(1, domainDtoPage.getTotalPages());
    Assertions.assertEquals(2, domainDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllDomainsWithTitleFilter() {
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

    List<Domain> domainList = List.of(
        new Domain(null, radar, "My first domain title", "Description", 0, null),
        new Domain(null, radar, "My second domain title", "New description", 1, null)
    );
    for (Domain domain : domainList) {
      domainRepository.save(domain);
    }

    DomainFilter domainFilter = new DomainFilter();
    domainFilter.setTitle(domainList.iterator().next().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<DomainDto> domainDtoPage = domainService.findAll(domainFilter, pageable);
    Assertions.assertEquals(10, domainDtoPage.getSize());
    Assertions.assertEquals(0, domainDtoPage.getNumber());
    Assertions.assertEquals(1, domainDtoPage.getTotalPages());
    Assertions.assertEquals(1, domainDtoPage.getNumberOfElements());
    Assertions.assertNotNull(domainDtoPage.iterator().next().getId());
    Assertions.assertEquals(domainDtoPage.iterator().next().getTitle(), domainList.iterator().next().getTitle());
    Assertions.assertEquals(domainDtoPage.iterator().next().getDescription(),
        domainList.iterator().next().getDescription());
  }
   */

  @Test
  void shouldFindByIdDomain() {
    final Domain domain = new Domain();
    domain.setId(10L);
    domain.setTitle("My title");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(domainRepository.findById(domain.getId())).thenReturn(Optional.of(domain));

    Optional<DomainDto> domainDtoOptional = domainService.findById(domain.getId());
    Assertions.assertTrue(domainDtoOptional.isPresent());
    Assertions.assertEquals(domain.getId(), domainDtoOptional.get().getId());
    Assertions.assertEquals(domain.getTitle(), domainDtoOptional.get().getTitle());
    Assertions.assertEquals(domain.getDescription(), domainDtoOptional.get().getDescription());
    Assertions.assertEquals(domain.getPosition(), domainDtoOptional.get().getPosition());
    Mockito.verify(domainRepository).findById(domain.getId());
  }

  @Test
  void shouldFindByTitleDomain() {
    final Domain domain = new Domain();
    domain.setId(10L);
    domain.setTitle("My title");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(domainRepository.findByTitle(domain.getTitle())).thenReturn(Optional.of(domain));

    Optional<DomainDto> domainDtoOptional = domainService.findByTitle(domain.getTitle());
    Assertions.assertTrue(domainDtoOptional.isPresent());
    Assertions.assertEquals(domain.getId(), domainDtoOptional.get().getId());
    Assertions.assertEquals(domain.getTitle(), domainDtoOptional.get().getTitle());
    Assertions.assertEquals(domain.getDescription(), domainDtoOptional.get().getDescription());
    Assertions.assertEquals(domain.getPosition(), domainDtoOptional.get().getPosition());
    Mockito.verify(domainRepository).findByTitle(domain.getTitle());
  }

  @Test
  void shouldSaveDomain() {
    /* TODO: uncomment
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(false);

    final Domain domain = new Domain();
    domain.setId(2L);
    domain.setRadar(radar);
    domain.setTitle("My title");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(domainRepository.save(any())).thenReturn(domain);
    Mockito.when(radarRepository.findById(radar.getId())).thenReturn(Optional.of(radar));

    DomainDto domainDto = domainService.save(domainMapper.toDto(domain));
    Assertions.assertEquals(domain.getId(), domainDto.getId());
    Assertions.assertEquals(domain.getTitle(), domainDto.getTitle());
    Assertions.assertEquals(domain.getDescription(), domainDto.getDescription());
    Assertions.assertEquals(domain.getPosition(), domainDto.getPosition());

    Mockito.verify(domainRepository).save(any());
    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
     */
  }

  @Test
  void shouldFailToSaveDomainDueToRadarIsActive() {
        /* TODO: uncomment

    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(null);
    radar.setTitle("Radar title");
    radar.setDescription("Radar description");
    radar.setPrimary(true);
    radar.setActive(true);

    final Domain domain = new Domain();
    domain.setId(2L);
    domain.setRadar(radar);
    domain.setTitle("My title");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(radarRepository.findById(radar.getId())).thenReturn(Optional.of(radar));
    Mockito.when(domainRepository.findById(domain.getId())).thenReturn(Optional.of(domain));

    ValidationException exception =
        catchThrowableOfType(() -> domainService.save(domainMapper.toDto(domain)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());
    Mockito.verify(domainRepository).findById(domain.getId());

         */
  }

  @Test
  void shouldFailToSaveDomainDueToBelongActiveRadar() {
        /* TODO: uncomment

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

    final Domain domain = new Domain();
    domain.setId(3L);
    domain.setRadar(radarActive);
    domain.setTitle("ADOPT");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(radarRepository.findById(domain.getRadar().getId())).thenReturn(Optional.of(radar));
    Mockito.when(domainRepository.findById(domain.getId())).thenReturn(Optional.of(domain));

    ValidationException exception =
            catchThrowableOfType(() -> domainService.save(domainMapper.toDto(domain)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("can't be saved for active radar"));

    Mockito.verify(radarRepository, Mockito.times(2)).findById(radarActive.getId());

         */
  }

  @Test
  void shouldFailToSaveDomainDueToTitleWithWhiteSpace() {
        /* TODO: uncomment

    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Domain domain = new Domain();
    domain.setId(2L);
    domain.setRadar(radar);
    domain.setTitle(" My title ");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    ValidationException exception =
        catchThrowableOfType(() -> domainService.save(domainMapper.toDto(domain)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));

    Mockito.verify(radarRepository, times(2)).findById(radar.getId());

         */
  }

  @Test
  void shouldDeleteDomain() {
        /* TODO: uncomment

    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(false);

    final Domain domain = new Domain();
    domain.setId(10L);
    domain.setRadar(radar);
    domain.setTitle("My title");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(domainRepository.findById(any())).thenReturn(Optional.of(domain));
    Mockito.doAnswer((i) -> null).when(domainRepository).deleteById(domain.getId());

    domainService.deleteById(domain.getId());
    Mockito.verify(domainRepository).findById(domain.getId());
    Mockito.verify(domainRepository).deleteById(domain.getId());
         */
  }

  @Test
  void shouldFailToDeleteDomainDueToRadarIsActive() {
        /* TODO: uncomment

    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setTitle("My radar title");
    radar.setPrimary(true);
    radar.setActive(true);

    final Domain domain = new Domain();
    domain.setId(10L);
    domain.setRadar(radar);
    domain.setTitle("My title");
    domain.setDescription("My description");
    domain.setPosition(1);

    Mockito.when(domainRepository.findById(any())).thenReturn(Optional.of(domain));

    ValidationException exception =
        catchThrowableOfType(() -> domainService.deleteById(domain.getId()), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertEquals(exception.getMessage(), "Domain can't be deleted for active radar.");
    Assertions.assertTrue(domain.getId().describeConstable().isPresent());

    Mockito.verify(domainRepository).findById(domain.getId());

         */
  }
}
