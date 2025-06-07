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
  private RadarUserRepository technologyRepository;
  @Autowired
  private RadarUserService technologyService;

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithNullFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null, "My title", "My website", "My description", 0, true),
        new RadarUser(null, "My new title", "My new website", "My new description", 0, true));
    for (RadarUser technology : technologyList) {
      technologyRepository.save(technology);
    }

    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarUserDto> technologyDtoPage = technologyService.findAll(null, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(2, technologyDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithBlankTitleFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null, "My title", "My website",  "My description", 0, true),
        new RadarUser(null, "My new title", "My new website",  "My new description", 0, true));
    for (RadarUser technology : technologyList) {
      technologyRepository.save(technology);
    }

    RadarUserFilter technologyFilter = new RadarUserFilter();
    technologyFilter.setTitle("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarUserDto> technologyDtoPage = technologyService.findAll(technologyFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(2, technologyDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithTitleFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null,  "My title",  "My website", "My description", 0, true),
        new RadarUser(null, "My new title",  "My new website", "My new description", 0, true));
    for (RadarUser technology : technologyList) {
      technologyRepository.save(technology);
    }

    RadarUserFilter technologyFilter = new RadarUserFilter();
    technologyFilter.setTitle(technologyList.iterator().next().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarUserDto> technologyDtoPage = technologyService.findAll(technologyFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(1, technologyDtoPage.getNumberOfElements());
    Assertions.assertNotNull(technologyDtoPage.iterator().next().getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getTitle(),
        technologyList.iterator().next().getTitle());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getDescription(),
        technologyList.iterator().next().getDescription());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithBlankWebsiteFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null,  "My first title",  "My first website", "My description", 0, true),
        new RadarUser(null, "My second title",  "My second website", "My new description", 0, true));
    for (RadarUser technology : technologyList) {
      technologyRepository.save(technology);
    }

    RadarUserFilter technologyFilter = new RadarUserFilter();
    technologyFilter.setWebsite("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarUserDto> technologyDtoPage = technologyService.findAll(technologyFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(2, technologyDtoPage.getNumberOfElements());
    Assertions.assertNotNull(technologyDtoPage.iterator().next().getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getWebsite(),
        technologyList.iterator().next().getWebsite());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getDescription(),
        technologyList.iterator().next().getDescription());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithWebsiteFilter() {
    List<RadarUser> technologyList = List.of(
        new RadarUser(null,  "My title",  "My first website", "My description", 0, true),
        new RadarUser(null, "My new title",  "My second website", "My new description", 0, true));
    for (RadarUser technology : technologyList) {
      technologyRepository.save(technology);
    }

    RadarUserFilter technologyFilter = new RadarUserFilter();
    technologyFilter.setWebsite(technologyList.iterator().next().getWebsite());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<RadarUserDto> technologyDtoPage = technologyService.findAll(technologyFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(1, technologyDtoPage.getNumberOfElements());
    Assertions.assertNotNull(technologyDtoPage.iterator().next().getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getTitle(),
        technologyList.iterator().next().getTitle());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getDescription(),
        technologyList.iterator().next().getDescription());
  }
}
