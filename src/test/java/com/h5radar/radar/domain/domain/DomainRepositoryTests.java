package com.h5radar.radar.domain.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.domain.AbstractRepositoryTests;
import com.h5radar.radar.domain.radar.Radar;
import com.h5radar.radar.domain.radar.RadarRepository;
import com.h5radar.radar.domain.radar_type.RadarType;
import com.h5radar.radar.domain.radar_type.RadarTypeRepository;

class DomainRepositoryTests extends AbstractRepositoryTests {
  @Autowired
  private RadarRepository radarRepository;

  @Autowired
  private RadarTypeRepository radarTypeRepository;

  @Autowired
  private DomainRepository domainRepository;

  @Test
  void shouldSaveDomainWithAllFields() {
    final RadarType radarType = new RadarType();
    radarType.setTitle("Technology radars 1");
    radarType.setCode("technology_radar_1");
    radarType.setDescription("Technology radars");
    radarTypeRepository.saveAndFlush(radarType);

    final Radar radar = new Radar();
    radar.setRadarType(radarType);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setPrimary(true);
    radar.setActive(false);
    radarRepository.saveAndFlush(radar);

    final Domain domain = new Domain();
    domain.setTitle("My domain title");
    domain.setRadar(radar);
    domain.setDescription("My domain description");

    Assertions.assertNull(domain.getId());
    Domain saved = domainRepository.saveAndFlush(domain);
    Assertions.assertNotNull(saved.getId());
    Assertions.assertNotNull(saved.getRadar());
    Assertions.assertNotNull(saved.getTitle());
    Assertions.assertNotNull(saved.getDescription());
    Assertions.assertNotNull(saved.getCreatedBy());
    Assertions.assertNotNull(saved.getCreatedDate());
    Assertions.assertNotNull(saved.getLastModifiedBy());
    Assertions.assertNotNull(saved.getLastModifiedDate());
  }

  @Test
  void shouldFailOnNullTitle() {
    final Domain domain = new Domain();
    domain.setDescription("My awesome description");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain)).isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnNullDescription() {
    final Domain domain = new Domain();
    domain.setTitle("My new test Domain");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain)).isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnEmptyTitle() {
    final Domain domain = new Domain();
    domain.setTitle("");
    domain.setDescription("My awesome description");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain)).isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnWhiteSpaceTitle() {
    final Domain domain = new Domain();
    domain.setTitle(" ");
    domain.setDescription("My awesome description");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain)).isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnEmptyDescription() {
    final Domain domain = new Domain();
    domain.setTitle("Hello");
    domain.setDescription("");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain)).isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnWhiteSpaceDescription() {
    final Domain domain = new Domain();
    domain.setTitle("Hello");
    domain.setDescription(" ");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain)).isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveDomainDueToTitleWithRightWhiteSpace() {
    final Domain domain = new Domain();
    domain.setTitle("My new test Domain ");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain))
            .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveDomainDueToTitleWithLeftWhiteSpace() {
    final Domain domain = new Domain();
    domain.setTitle(" My new test Domain");

    Assertions.assertNull(domain.getId());
    assertThatThrownBy(() -> domainRepository.saveAndFlush(domain))
            .isInstanceOf(ValidationException.class);
  }
}
