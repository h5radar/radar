package com.h5radar.radar.radar_user;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.AbstractServiceTests;
import com.h5radar.radar.ValidationException;

class RadarUserServiceTests extends AbstractServiceTests {
  @MockitoBean
  private MessageSource messageSource;

  @MockitoBean
  private RadarUserRepository radarUserRepository;

  @Autowired
  private RadarUserMapper radarUserMapper;

  @Autowired
  private RadarUserService radarUserService;

  @Test
  void shouldFindAllRadarUsers() {
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
  void shouldFindAllRadarUsersWithNullFilter() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    List<RadarUser> radarUserList = List.of(radarUser);
    Page<RadarUser> page = new PageImpl<>(radarUserList);
    Mockito.when(radarUserRepository.findAll(ArgumentMatchers.<Specification<RadarUser>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("sub,asc"));
    Page<RadarUserDto> radarUserDtoPage = radarUserService.findAll(null, pageable);
    // Mockito.verify(radarUserRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);

    Assertions.assertEquals(1, radarUserDtoPage.getSize());
    Assertions.assertEquals(0, radarUserDtoPage.getNumber());
    Assertions.assertEquals(1, radarUserDtoPage.getTotalPages());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getId(), radarUser.getId());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getSub(), radarUser.getSub());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getUsername(), radarUser.getUsername());

  }

  @Test
  void shouldFindAllRadarUsersWithEmptyFilter() {
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
    // Mockito.verify(radarUserRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);

    Assertions.assertEquals(1, radarUserDtoPage.getSize());
    Assertions.assertEquals(0, radarUserDtoPage.getNumber());
    Assertions.assertEquals(1, radarUserDtoPage.getTotalPages());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getId(), radarUser.getId());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getSub(), radarUser.getSub());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getUsername(), radarUser.getUsername());

  }

  /* TODO:


  @Test
  // @Transactional
  void shouldFindAllRadarUsersWithBlankSubFilter() {
    List<RadarUser> radarUserList = List.of(
        new RadarUser(null, "My sub", "My username"),
        new RadarUser(null, "My new sub", "My new username"));
    for (RadarUser radarUser : radarUserList) {
      radarUserRepository.save(radarUser);
    }

    RadarUserFilter radarUserFilter = new RadarUserFilter();
    radarUserFilter.setSub("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<RadarUserDto> radarUserDtoPage = radarUserService.findAll(radarUserFilter, pageable);
    Assertions.assertEquals(10, radarUserDtoPage.getSize());
    Assertions.assertEquals(0, radarUserDtoPage.getNumber());
    Assertions.assertEquals(1, radarUserDtoPage.getTotalPages());
    Assertions.assertEquals(2, radarUserDtoPage.getNumberOfElements());
  }

  @Test
  // @Transactional
  void shouldFindAllRadarUsersWithTitleFilter() {
    List<RadarUser> radarUserList = List.of(
        new RadarUser(null,  "My sub",  "My username"),
        new RadarUser(null, "My new sub",  "My new username"));
    for (RadarUser radarUser : radarUserList) {
      radarUserRepository.save(radarUser);
    }

    RadarUserFilter radarUserFilter = new RadarUserFilter();
    radarUserFilter.setSub(radarUserList.iterator().next().getSub());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<RadarUserDto> radarUserDtoPage = radarUserService.findAll(radarUserFilter, pageable);
    Assertions.assertEquals(10, radarUserDtoPage.getSize());
    Assertions.assertEquals(0, radarUserDtoPage.getNumber());
    Assertions.assertEquals(1, radarUserDtoPage.getTotalPages());
    Assertions.assertEquals(1, radarUserDtoPage.getNumberOfElements());
    Assertions.assertNotNull(radarUserDtoPage.iterator().next().getId());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getSub(),
        radarUserList.iterator().next().getSub());
    Assertions.assertEquals(radarUserDtoPage.iterator().next().getUsername(),
        radarUserList.iterator().next().getUsername());
  }
  */

  @Test
  void shouldFindByIdRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Mockito.when(radarUserRepository.findById(radarUser.getId())).thenReturn(Optional.of(radarUser));
    Optional<RadarUserDto> radarUserDtoOptional = radarUserService.findById(radarUser.getId());
    Mockito.verify(radarUserRepository).findById(radarUser.getId());

    Assertions.assertTrue(radarUserDtoOptional.isPresent());
    Assertions.assertEquals(radarUser.getId(), radarUserDtoOptional.get().getId());
    Assertions.assertEquals(radarUser.getSub(), radarUserDtoOptional.get().getSub());
    Assertions.assertEquals(radarUser.getUsername(), radarUserDtoOptional.get().getUsername());

  }

  @Test
  void shouldFindBySubRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Mockito.when(radarUserRepository.findBySub(radarUser.getSub())).thenReturn(Optional.of(radarUser));
    Optional<RadarUserDto> radarUserDtoOptional = radarUserService.findBySub(radarUser.getSub());
    Mockito.verify(radarUserRepository).findBySub(radarUser.getSub());

    Assertions.assertTrue(radarUserDtoOptional.isPresent());
    Assertions.assertEquals(radarUser.getId(), radarUserDtoOptional.get().getId());
    Assertions.assertEquals(radarUser.getSub(), radarUserDtoOptional.get().getSub());
    Assertions.assertEquals(radarUser.getUsername(), radarUserDtoOptional.get().getUsername());

  }

  @Test
  void shouldSaveRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(10L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Mockito.when(radarUserRepository.save(any())).thenReturn(radarUser);
    RadarUserDto radarUserDto = radarUserService.save(radarUserMapper.toDto(radarUser));
    Mockito.verify(radarUserRepository).save(any());

    Assertions.assertEquals(radarUser.getId(), radarUserDto.getId());
    Assertions.assertEquals(radarUser.getSub(), radarUserDto.getSub());
    Assertions.assertEquals(radarUser.getUsername(), radarUserDto.getUsername());

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
  void shouldSeedRadarUser() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUser.setSeeded(false);
    radarUser.setSeededDate(null);

    Mockito.when(radarUserRepository.findById(radarUser.getId())).thenReturn(Optional.of(radarUser));
    Mockito.when(radarUserRepository.save(Mockito.any())).thenReturn(radarUser);

    radarUserService.updateSeed(radarUser.getId());

    Mockito.verify(radarUserRepository).save(Mockito.any());
    Assertions.assertTrue(radarUser.isSeeded());
    Assertions.assertNotNull(radarUser.getSeededDate());
  }

  @Test
  void shouldSeedRadarUserWithoutOverwritingExistingSeededDate() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(2L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUser.setSeeded(false);
    final Instant existingDate = Instant.parse("2025-02-02T02:02:02Z");
    radarUser.setSeededDate(existingDate);

    Mockito.when(radarUserRepository.findById(radarUser.getId())).thenReturn(Optional.of(radarUser));
    Mockito.when(radarUserRepository.save(Mockito.any())).thenReturn(radarUser);

    radarUserService.updateSeed(radarUser.getId());

    Mockito.verify(radarUserRepository).save(Mockito.any());
    Assertions.assertTrue(radarUser.isSeeded());
    Assertions.assertEquals(existingDate, radarUser.getSeededDate());
  }

  @Test
  void shouldSeedRadarUserWhenAlreadySeededDoNothing() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(3L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUser.setSeeded(true);
    final Instant seededDate = Instant.parse("2025-01-01T00:00:00Z");
    radarUser.setSeededDate(seededDate);

    Mockito.when(radarUserRepository.findById(radarUser.getId())).thenReturn(Optional.of(radarUser));

    radarUserService.updateSeed(radarUser.getId());

    Mockito.verify(radarUserRepository, Mockito.never()).save(Mockito.any());
    Assertions.assertTrue(radarUser.isSeeded());
    Assertions.assertEquals(seededDate, radarUser.getSeededDate());
  }

  @Test
  void shouldFailToSeedRadarUserDueNotFound() {
    final Long id = 404L;

    Mockito.when(radarUserRepository.findById(id)).thenReturn(Optional.empty());
    Mockito.when(messageSource.getMessage(
        Mockito.eq("radar_user.error.not_found"),
        Mockito.any(Object[].class),
        Mockito.eq(LocaleContextHolder.getLocale())
    )).thenReturn("Radar user not found: " + id);

    IllegalStateException exception = Assertions.assertThrows(
        IllegalStateException.class,
        () -> radarUserService.updateSeed(id)
    );

    Mockito.verify(messageSource).getMessage(
        Mockito.eq("radar_user.error.not_found"),
        Mockito.any(Object[].class),
        Mockito.eq(LocaleContextHolder.getLocale())
    );
    Mockito.verify(radarUserRepository, Mockito.never()).save(Mockito.any());
    Assertions.assertTrue(exception.getMessage().contains(String.valueOf(id)));
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
