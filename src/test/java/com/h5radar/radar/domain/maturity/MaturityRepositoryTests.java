package com.h5radar.radar.domain.maturity;

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

class MaturityRepositoryTests extends AbstractRepositoryTests {
  @Autowired
  private RadarRepository radarRepository;

  @Autowired
  private RadarTypeRepository radarTypeRepository;

  @Autowired
  private MaturityRepository maturityRepository;

  @Test
  void shouldSaveMaturityWithAllFields() {
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

    final Maturity maturity = new Maturity();
    maturity.setTitle("ADOPT");
    maturity.setRadar(radar);
    maturity.setColor("d42d");
    maturity.setDescription("Very good description for maturity");

    Assertions.assertNull(maturity.getId());
    Maturity saved = maturityRepository.saveAndFlush(maturity);
    Assertions.assertNotNull(saved.getId());
    Assertions.assertNotNull(saved.getRadar());
    Assertions.assertNotNull(saved.getTitle());
    Assertions.assertNotNull(saved.getColor());
    Assertions.assertNotNull(saved.getDescription());
    Assertions.assertNotNull(saved.getCreatedBy());
    Assertions.assertNotNull(saved.getCreatedDate());
    Assertions.assertNotNull(saved.getLastModifiedBy());
    Assertions.assertNotNull(saved.getLastModifiedDate());
  }

  @Test
  void shouldFailOnNullTitle() {
    final Maturity r = new Maturity();
    r.setDescription("Very good description for Maturity");

    Assertions.assertNull(r.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(r))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnEmptyTitle() {
    final Maturity r = new Maturity();
    r.setTitle("");
    r.setDescription("Very good description for Maturity");

    Assertions.assertNull(r.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(r))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnWhiteSpaceTitle() {
    final Maturity r = new Maturity();
    r.setTitle(" ");
    r.setDescription("Very good description for Maturity");

    Assertions.assertNull(r.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(r))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnNullDescription() {
    final Maturity r = new Maturity();
    r.setTitle("TEST");

    Assertions.assertNull(r.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(r))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnEmptyDescription() {
    final Maturity r = new Maturity();
    r.setTitle("TEST");
    r.setDescription("");

    Assertions.assertNull(r.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(r))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnWhiteSpaceDescription() {
    final Maturity r = new Maturity();
    r.setTitle("TEST");
    r.setDescription(" ");

    Assertions.assertNull(r.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(r))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnLowerTitle() {
    final Maturity r = new Maturity();
    r.setTitle("test");
    r.setDescription("Very good description for Maturity");

    Assertions.assertNull(r.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(r))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFindSavedMaturityById() {
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

    final Maturity maturity = new Maturity();
    maturity.setTitle("ADOPT");
    maturity.setRadar(radar);
    maturity.setColor("d42d");
    maturity.setDescription("Very good description for maturity");

    Assertions.assertNull(maturity.getId());
    maturityRepository.saveAndFlush(maturity);
    Assertions.assertNotNull(maturity.getId());
    var id = maturity.getId();

    Assertions.assertTrue(maturityRepository.findById(id).isPresent());
  }

  @Test
  void shouldFindSavedMaturityByTitle() {
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

    String title = "SUPER";
    final Maturity maturity = new Maturity();
    maturity.setTitle(title);
    maturity.setRadar(radar);
    maturity.setColor("d42d");
    maturity.setDescription("Very good description for Maturity");

    Assertions.assertNull(maturity.getId());
    maturityRepository.saveAndFlush(maturity);
    Assertions.assertNotNull(maturity.getId());

    // todo: use service (not repository?)
    Assertions.assertTrue(maturityRepository.findByTitle(title).isPresent());
  }

  @Test
  void shouldFailToSaveMaturityDueToTitleWithRightWhiteSpace() {
    final Maturity maturity = new Maturity();
    maturity.setTitle("My new test Maturity ");

    Assertions.assertNull(maturity.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(maturity))
            .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveMaturityDueToTitleWithLeftWhiteSpace() {
    final Maturity maturity = new Maturity();
    maturity.setTitle(" My new test Maturity");

    Assertions.assertNull(maturity.getId());
    assertThatThrownBy(() -> maturityRepository.saveAndFlush(maturity))
            .isInstanceOf(ValidationException.class);
  }
}
