package com.h5radar.radar.technology;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;

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

import com.h5radar.radar.AbstractServiceTests;
import com.h5radar.radar.ValidationException;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;



class TechnologyServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RadarUserRepository radarUserRepository;
  @MockitoBean
  private TechnologyRepository technologyRepository;
  @Autowired
  private TechnologyMapper technologyMapper;
  @Autowired
  private TechnologyService technologyService;

  @Test
  void shouldFindAllTechnologies() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setRadarUser(radarUser);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    List<Technology> technologyList = List.of(technology);
    Mockito.when(technologyRepository.findAll(any(Sort.class))).thenReturn(technologyList);

    Collection<TechnologyDto> technologyDtoCollection = technologyService.findAll();
    Assertions.assertEquals(1, technologyDtoCollection.size());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getTitle(), technology.getTitle());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getDescription(), technology.getDescription());
  }

  @Test
  void shouldFindAllTechnologiesWithNullFilter() {
    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    List<Technology> technologyList = List.of(technology);
    Page<Technology> page = new PageImpl<>(technologyList);
    Mockito.when(technologyRepository.findAll(ArgumentMatchers.<Specification<Technology>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<TechnologyDto> technologyDtoPage = technologyService.findAll(null, pageable);
    Assertions.assertEquals(1, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getTitle(), technology.getTitle());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getDescription(), technology.getDescription());

    // Mockito.verify(technologyRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindAllTechnologiesWithEmptyFilter() {
    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    List<Technology> technologyList = List.of(technology);
    Page<Technology> page = new PageImpl<>(technologyList);
    Mockito.when(technologyRepository.findAll(ArgumentMatchers.<Specification<Technology>>any(), any(Pageable.class)))
        .thenReturn(page);

    TechnologyFilter technologyFilter = new TechnologyFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<TechnologyDto> technologyDtoPage = technologyService.findAll(technologyFilter, pageable);
    Assertions.assertEquals(1, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getTitle(), technology.getTitle());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getDescription(), technology.getDescription());

    // Mockito.verify(technologyRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindByIdTechnology() {
    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.when(technologyRepository.findById(technology.getId())).thenReturn(Optional.of(technology));

    Optional<TechnologyDto> technologyDtoOptional = technologyService.findById(technology.getId());
    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getTitle(), technologyDtoOptional.get().getTitle());
    Assertions.assertEquals(technology.getWebsite(), technologyDtoOptional.get().getWebsite());
    Assertions.assertEquals(technology.getDescription(), technologyDtoOptional.get().getDescription());
    Assertions.assertEquals(technology.getMoved(), technologyDtoOptional.get().getMoved());

    Mockito.verify(technologyRepository).findById(technology.getId());
  }

  @Test
  void shouldFindByTitleTechnology() {
    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.when(technologyRepository.findByTitle(technology.getTitle())).thenReturn(Optional.of(technology));

    Optional<TechnologyDto> technologyDtoOptional = technologyService.findByTitle(technology.getTitle());
    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getTitle(), technologyDtoOptional.get().getTitle());
    Assertions.assertEquals(technology.getWebsite(), technologyDtoOptional.get().getWebsite());
    Assertions.assertEquals(technology.getDescription(), technologyDtoOptional.get().getDescription());
    Assertions.assertEquals(technology.getMoved(), technologyDtoOptional.get().getMoved());

    Mockito.verify(technologyRepository).findByTitle(technology.getTitle());
  }

  @Test
  void shouldSaveTechnology() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(3L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setRadarUser(radarUser);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.when(radarUserRepository.findById(any())).thenReturn(Optional.of(radarUser));
    Mockito.when(technologyRepository.save(any())).thenReturn(technology);

    TechnologyDto technologyDto = technologyService.save(technologyMapper.toDto(technology));
    Assertions.assertEquals(technology.getId(), technologyDto.getId());
    Assertions.assertEquals(technology.getTitle(), technologyDto.getTitle());
    Assertions.assertEquals(technology.getWebsite(), technologyDto.getWebsite());
    Assertions.assertEquals(technology.getDescription(), technologyDto.getDescription());
    Assertions.assertEquals(technology.getMoved(), technologyDto.getMoved());

    Mockito.verify(radarUserRepository).findById(radarUser.getId());
    Mockito.verify(technologyRepository).save(any());
  }

  @Test
  void shouldFailToSaveTechnologyDueToTitleWithWhiteSpace() {
    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setTitle(" My technology ");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    ValidationException exception = catchThrowableOfType(() ->
        technologyService.save(technologyMapper.toDto(technology)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteTechnology() {
    final Technology technology = new Technology();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.doAnswer((i) -> null).when(technologyRepository).deleteById(technology.getId());

    technologyService.deleteById(technology.getId());
    Mockito.verify(technologyRepository).deleteById(technology.getId());
  }
}
