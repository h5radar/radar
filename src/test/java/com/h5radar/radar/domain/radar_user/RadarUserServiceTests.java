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
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    List<RadarUser> radarUserList = List.of(radarUser);
    Mockito.when(radarUserRepository.findAll(any(Sort.class))).thenReturn(radarUserList);

    Collection<RadarUserDto> radarUserDtoCollection = radarUserService.findAll();
    Assertions.assertEquals(1, radarUserDtoCollection.size());
    Assertions.assertEquals(radarUserDtoCollection.iterator().next().getId(), radarUser.getId());
    Assertions.assertEquals(radarUserDtoCollection.iterator().next().getSub(), radarUser.getSub());
    Assertions.assertEquals(radarUserDtoCollection.iterator().next().getUsername(), radarUser.getUsername());
  }

  @Test
  void shouldFindAllTechnologiesWithEmptyFilter() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    List<RadarUser> radarUserList = List.of(radarUser);
    Page<RadarUser> page = new PageImpl<>(radarUserList);
    Mockito.when(radarUserRepository.findAll(ArgumentMatchers.<Specification<RadarUser>>any(), any(Pageable.class)))
        .thenReturn(page);

    RadarUserFilter radarUserFilter = new RadarUserFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("sub,asc"));
    Page<RadarUserDto> radarUserDtoPage = radarUserService.findAll(radarUserFilter, pageable);
    Assertions.assertEquals(1, radarUserDtoPage.getSize());
    Assertions.assertEquals(0, radarUserDtoPage.getNumber());
    Assertions.assertEquals(1, radarUserDtoPage.getTotalPages());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getId(), radarUser.getId());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getSub(), radarUser.getSub());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getUsername(), radarUser.getUsername());

    // Mockito.verify(radarUserRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindByIdRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Mockito.when(radarUserRepository.findById(radarUser.getId())).thenReturn(Optional.of(radarUser));

    Optional<RadarUserDto> radarUserDtoOptional = radarUserService.findById(radarUser.getId());
    Assertions.assertTrue(radarUserDtoOptional.isPresent());
    Assertions.assertEquals(radarUser.getId(), radarUserDtoOptional.get().getId());
    Assertions.assertEquals(radarUser.getSub(), radarUserDtoOptional.get().getSub());
    Assertions.assertEquals(radarUser.getUsername(), radarUserDtoOptional.get().getUsername());

    Mockito.verify(radarUserRepository).findById(radarUser.getId());
  }

  @Test
  void shouldfindBySubRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Mockito.when(radarUserRepository.findBySub(radarUser.getSub())).thenReturn(Optional.of(radarUser));

    Optional<RadarUserDto> radarUserDtoOptional = radarUserService.findBySub(radarUser.getSub());
    Assertions.assertTrue(radarUserDtoOptional.isPresent());
    Assertions.assertEquals(radarUser.getId(), radarUserDtoOptional.get().getId());
    Assertions.assertEquals(radarUser.getSub(), radarUserDtoOptional.get().getSub());
    Assertions.assertEquals(radarUser.getUsername(), radarUserDtoOptional.get().getUsername());

    Mockito.verify(radarUserRepository).findBySub(radarUser.getSub());
  }

  @Test
  void shouldSaveRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Mockito.when(radarUserRepository.save(any())).thenReturn(radarUser);

    RadarUserDto radarUserDto = radarUserService.save(radarUserMapper.toDto(radarUser));
    Assertions.assertEquals(radarUser.getId(), radarUserDto.getId());
    Assertions.assertEquals(radarUser.getSub(), radarUserDto.getSub());
    Assertions.assertEquals(radarUser.getUsername(), radarUserDto.getUsername());

    Mockito.verify(radarUserRepository).save(any());
  }

  @Test
  void shouldFailToSaveRadarUserDueToTitleWithWhiteSpace() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub(" My sub ");
    radarUser.setUsername("My username");

    ValidationException exception = catchThrowableOfType(() ->
        radarUserService.save(radarUserMapper.toDto(radarUser)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Mockito.doAnswer((i) -> null).when(radarUserRepository).deleteById(radarUser.getId());

    radarUserService.deleteById(radarUser.getId());
    Mockito.verify(radarUserRepository).deleteById(radarUser.getId());
  }
}
