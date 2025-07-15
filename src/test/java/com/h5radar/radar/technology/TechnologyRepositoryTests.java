package com.h5radar.radar.domain.technology;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.domain.AbstractRepositoryTests;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;

class TechnologyRepositoryTests extends AbstractRepositoryTests {
  @Autowired
  private RadarUserRepository radarUserRepository;

  @Autowired
  private TechnologyRepository technologyRepository;

  @Test
  void shouldSaveTechnologyWithAllFields() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setTitle("My title");
    technology.setDescription("My description");

    Assertions.assertNull(technology.getId());
    technologyRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    Assertions.assertNotNull(technology.getRadarUser());
    Assertions.assertNotNull(technology.getTitle());
    Assertions.assertNotNull(technology.getDescription());
    Assertions.assertNotNull(technology.getCreatedBy());
    Assertions.assertNotNull(technology.getCreatedDate());
    Assertions.assertNotNull(technology.getLastModifiedBy());
    Assertions.assertNotNull(technology.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedTechnologyById() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setTitle("My title");
    technology.setDescription("My description");

    Assertions.assertNull(technology.getId());
    technologyRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    Assertions.assertTrue(technologyRepository.findById(technology.getId()).isPresent());
  }

  @Test
  void shouldFailOnNullTitle() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setDescription("My description");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("title", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be blank", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailOnEmptyTitle() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setTitle("");
    technology.setDescription("My description");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(2, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("title", constraintViolation.getPropertyPath().toString());
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 64"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceTitle() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setTitle(" ");
    technology.setDescription("My description");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(2, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("title", constraintViolation.getPropertyPath().toString());
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("should be without whitespaces before and after"));
    }
  }

  @Test
  void shouldFailOnNullDescription() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setTitle("My title");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "description");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailOnEmptyDescription() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setTitle("My title");
    technology.setDescription("");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(2, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("description", constraintViolation.getPropertyPath().toString());
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 512"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceDescription() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create technology
    final Technology technology = new Technology();
    technology.setRadarUser(radarUser);
    technology.setTitle("My title");
    technology.setDescription(" ");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("description", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be blank", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailToSaveTechnologyDueToTitleWithRightWhiteSpace() {
    final Technology technology = new Technology();
    technology.setTitle("My title with right white space");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> technologyRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveTechnologyDueToTitleWithLeftWhiteSpace() {
    final Technology technology = new Technology();
    technology.setTitle(" My title with left white space");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> technologyRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }
}
