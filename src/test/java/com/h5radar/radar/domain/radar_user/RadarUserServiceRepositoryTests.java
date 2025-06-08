package com.h5radar.radar.domain.radar_user;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.h5radar.radar.domain.AbstractServiceTests;

class RadarUserServiceRepositoryTests extends AbstractServiceTests {
  @Autowired
  private RadarUserRepository radarUserRepository;
  @Autowired
  private RadarUserService radarUserService;

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithNullFilter() {
    List<RadarUser> radarUserList = List.of(
        new RadarUser(null, "My sub", "My username"),
        new RadarUser(null, "My new sub", "My new username"));
    for (RadarUser radarUser : radarUserList) {
      radarUserRepository.save(radarUser);
    }

    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<RadarUserDto> radarUserDtoPage = radarUserService.findAll(null, pageable);
    Assertions.assertEquals(10, radarUserDtoPage.getSize());
    Assertions.assertEquals(0, radarUserDtoPage.getNumber());
    Assertions.assertEquals(1, radarUserDtoPage.getTotalPages());
    Assertions.assertEquals(2, radarUserDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithBlankSubFilter() {
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
  @Transactional
  void shouldFindAllTechnologiesWithTitleFilter() {
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
}
