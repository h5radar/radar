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
    List<RadarUser> technologyList = List.of(
        new RadarUser(null, "My title", "My description"),
        new RadarUser(null, "My new title", "My new description"));
    for (RadarUser technology : technologyList) {
      radarUserRepository.save(technology);
    }

    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarUserDto> technologyDtoPage = radarUserService.findAll(null, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(2, technologyDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithBlankTitleFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null, "My title", "My description"),
        new RadarUser(null, "My new title", "My new description"));
    for (RadarUser technology : technologyList) {
      radarUserRepository.save(technology);
    }

    RadarUserFilter radarUserFilter = new RadarUserFilter();
    radarUserFilter.setSub("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
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
        new RadarUser(null,  "My title",  "My description"),
        new RadarUser(null, "My new title",  "My new description"));
    for (RadarUser technology : technologyList) {
      radarUserRepository.save(technology);
    }

    RadarUserFilter radarUserFilter = new RadarUserFilter();
    radarUserFilter.setSub(technologyList.iterator().next().getSub());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
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
}
