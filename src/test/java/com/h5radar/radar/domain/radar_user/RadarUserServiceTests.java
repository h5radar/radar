package com.h5radar.radar.domain.radar_user;

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

import com.h5radar.radar.domain.AbstractServiceTests;
import com.h5radar.radar.domain.ValidationException;

class RadarUserServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RadarUserRepository technologyRepository;
  @Autowired
  private RadarUserMapper technologyMapper;
  @Autowired
  private RadarUserService radarUserService;

  @Test
  void shouldFindAllTechnologies() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    List<RadarUser> technologyList = List.of(technology);
    Mockito.when(technologyRepository.findAll(any(Sort.class))).thenReturn(technologyList);

    Collection<RadarUserDto> technologyDtoCollection = radarUserService.findAll();
    Assertions.assertEquals(1, technologyDtoCollection.size());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getTitle(), technology.getTitle());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getDescription(), technology.getDescription());
  }

  @Test
  void shouldFindAllTechnologiesWithEmptyFilter() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    List<RadarUser> technologyList = List.of(technology);
    Page<RadarUser> page = new PageImpl<>(technologyList);
    Mockito.when(technologyRepository.findAll(ArgumentMatchers.<Specification<RadarUser>>any(), any(Pageable.class)))
        .thenReturn(page);

    RadarUserFilter technologyFilter = new RadarUserFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<RadarUserDto> technologyDtoPage = radarUserService.findAll(technologyFilter, pageable);
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
  void shouldFindByIdRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.when(technologyRepository.findById(technology.getId())).thenReturn(Optional.of(technology));

    Optional<RadarUserDto> technologyDtoOptional = radarUserService.findById(technology.getId());
    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getTitle(), technologyDtoOptional.get().getTitle());
    Assertions.assertEquals(technology.getWebsite(), technologyDtoOptional.get().getWebsite());
    Assertions.assertEquals(technology.getDescription(), technologyDtoOptional.get().getDescription());
    Assertions.assertEquals(technology.getMoved(), technologyDtoOptional.get().getMoved());

    Mockito.verify(technologyRepository).findById(technology.getId());
  }

  @Test
  void shouldFindByTitleRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.when(technologyRepository.findByTitle(technology.getTitle())).thenReturn(Optional.of(technology));

    Optional<RadarUserDto> technologyDtoOptional = radarUserService.findByTitle(technology.getTitle());
    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getTitle(), technologyDtoOptional.get().getTitle());
    Assertions.assertEquals(technology.getWebsite(), technologyDtoOptional.get().getWebsite());
    Assertions.assertEquals(technology.getDescription(), technologyDtoOptional.get().getDescription());
    Assertions.assertEquals(technology.getMoved(), technologyDtoOptional.get().getMoved());

    Mockito.verify(technologyRepository).findByTitle(technology.getTitle());
  }

  @Test
  void shouldSaveRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.when(technologyRepository.save(any())).thenReturn(technology);

    RadarUserDto technologyDto = radarUserService.save(technologyMapper.toDto(technology));
    Assertions.assertEquals(technology.getId(), technologyDto.getId());
    Assertions.assertEquals(technology.getTitle(), technologyDto.getTitle());
    Assertions.assertEquals(technology.getWebsite(), technologyDto.getWebsite());
    Assertions.assertEquals(technology.getDescription(), technologyDto.getDescription());
    Assertions.assertEquals(technology.getMoved(), technologyDto.getMoved());

    Mockito.verify(technologyRepository).save(any());
  }

  @Test
  void shouldFailToSaveRadarUserDueToTitleWithWhiteSpace() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setTitle(" My technology ");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    ValidationException exception = catchThrowableOfType(() ->
        radarUserService.save(technologyMapper.toDto(technology)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setTitle("My technology");
    technology.setWebsite("My website");
    technology.setDescription("My technology description");
    technology.setMoved(0);
    technology.setActive(true);

    Mockito.doAnswer((i) -> null).when(technologyRepository).deleteById(technology.getId());

    radarUserService.deleteById(technology.getId());
    Mockito.verify(technologyRepository).deleteById(technology.getId());
  }
}
