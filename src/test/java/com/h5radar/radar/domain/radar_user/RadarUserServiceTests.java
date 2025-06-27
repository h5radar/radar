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
  private RadarUserRepository radarUserRepository;

  @Autowired
  private RadarUserMapper radarUserMapper;

  @Autowired
  private RadarUserService radarUserService;

  @Test
  void shouldFindAllTechnologies() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    List<RadarUser> technologyList = List.of(technology);
    Mockito.when(radarUserRepository.findAll(any(Sort.class))).thenReturn(technologyList);

    Collection<RadarUserDto> technologyDtoCollection = radarUserService.findAll();
    Assertions.assertEquals(1, technologyDtoCollection.size());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getSub(), technology.getSub());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getUsername(), technology.getUsername());
  }

  @Test
  void shouldFindAllTechnologiesWithNullFilter() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    List<RadarUser> technologyList = List.of(technology);
    Page<RadarUser> page = new PageImpl<>(technologyList);
    Mockito.when(radarUserRepository.findAll(ArgumentMatchers.<Specification<RadarUser>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("sub,asc"));
    Page<RadarUserDto> technologyDtoPage = radarUserService.findAll(null, pageable);
    Assertions.assertEquals(1, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getSub(), technology.getSub());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getUsername(), technology.getUsername());

    // Mockito.verify(radarUserRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindAllTechnologiesWithEmptyFilter() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    List<RadarUser> technologyList = List.of(technology);
    Page<RadarUser> page = new PageImpl<>(technologyList);
    Mockito.when(radarUserRepository.findAll(ArgumentMatchers.<Specification<RadarUser>>any(), any(Pageable.class)))
        .thenReturn(page);

    RadarUserFilter technologyFilter = new RadarUserFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("sub,asc"));
    Page<RadarUserDto> technologyDtoPage = radarUserService.findAll(technologyFilter, pageable);
    Assertions.assertEquals(1, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getSub(), technology.getSub());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getUsername(), technology.getUsername());

    // Mockito.verify(radarUserRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  /* TODO:


  @Test
  @Transactional
  void shouldFindAllTechnologiesWithBlankSubFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null, "My sub", "My username"),
        new RadarUser(null, "My new sub", "My new username"));
    for (RadarUser technology : technologyList) {
      radarUserRepository.save(technology);
    }

    RadarUserFilter radarUserFilter = new RadarUserFilter();
    radarUserFilter.setSub("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<RadarUserDto> technologyDtoPage = radarUserService.findAll(radarUserFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(2, technologyDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithTitleFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null,  "My sub",  "My username"),
        new RadarUser(null, "My new sub",  "My new username"));
    for (RadarUser technology : technologyList) {
      radarUserRepository.save(technology);
    }

    RadarUserFilter radarUserFilter = new RadarUserFilter();
    radarUserFilter.setSub(technologyList.iterator().next().getSub());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<RadarUserDto> technologyDtoPage = radarUserService.findAll(radarUserFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(1, technologyDtoPage.getNumberOfElements());
    Assertions.assertNotNull(technologyDtoPage.iterator().next().getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getSub(),
        technologyList.iterator().next().getSub());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getUsername(),
        technologyList.iterator().next().getUsername());
  }
  */

  @Test
  void shouldFindByIdRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.when(radarUserRepository.findById(technology.getId())).thenReturn(Optional.of(technology));

    Optional<RadarUserDto> technologyDtoOptional = radarUserService.findById(technology.getId());
    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getSub(), technologyDtoOptional.get().getSub());
    Assertions.assertEquals(technology.getUsername(), technologyDtoOptional.get().getUsername());

    Mockito.verify(radarUserRepository).findById(technology.getId());
  }

  @Test
  void shouldfindBySubRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.when(radarUserRepository.findBySub(technology.getSub())).thenReturn(Optional.of(technology));

    Optional<RadarUserDto> technologyDtoOptional = radarUserService.findBySub(technology.getSub());
    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getSub(), technologyDtoOptional.get().getSub());
    Assertions.assertEquals(technology.getUsername(), technologyDtoOptional.get().getUsername());

    Mockito.verify(radarUserRepository).findBySub(technology.getSub());
  }

  @Test
  void shouldSaveRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.when(radarUserRepository.save(any())).thenReturn(technology);

    RadarUserDto technologyDto = radarUserService.save(radarUserMapper.toDto(technology));
    Assertions.assertEquals(technology.getId(), technologyDto.getId());
    Assertions.assertEquals(technology.getSub(), technologyDto.getSub());
    Assertions.assertEquals(technology.getUsername(), technologyDto.getUsername());

    Mockito.verify(radarUserRepository).save(any());
  }

  @Test
  void shouldFailToSaveRadarUserDueToTitleWithWhiteSpace() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub(" My sub ");
    technology.setUsername("My username");

    ValidationException exception = catchThrowableOfType(() ->
        radarUserService.save(radarUserMapper.toDto(technology)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteRadarUser() {
    final RadarUser technology = new RadarUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.doAnswer((i) -> null).when(radarUserRepository).deleteById(technology.getId());

    radarUserService.deleteById(technology.getId());
    Mockito.verify(radarUserRepository).deleteById(technology.getId());
  }
}
