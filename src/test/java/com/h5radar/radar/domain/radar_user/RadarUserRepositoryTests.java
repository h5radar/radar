package com.h5radar.radar.domain.radar_user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.domain.AbstractRepositoryTests;

class RadarUserRepositoryTests extends AbstractRepositoryTests {

  @Autowired
  private RadarUserRepository technologyRepository;

  @Test
  void shouldSaveRadarUserWithAllFields() {
    final RadarUser technology = new RadarUser();
    technology.setTitle("TEST");
    technology.setDescription("Very good description for RadarUser");

    Assertions.assertNull(technology.getId());
    technologyRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    Assertions.assertNotNull(technology.getCreatedBy());
    Assertions.assertNotNull(technology.getCreatedDate());
    Assertions.assertNotNull(technology.getLastModifiedBy());
    Assertions.assertNotNull(technology.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedRadarUserById() {
    final RadarUser technology = new RadarUser();
    technology.setTitle("MY");
    technology.setDescription("Very good description for RadarUser");

    Assertions.assertNull(technology.getId());
    technologyRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    var id = technology.getId();

    Assertions.assertTrue(technologyRepository.findById(id).isPresent());
  }

  @Test
  void shouldFailOnNullTitle() {
    final RadarUser technology = new RadarUser();
    technology.setDescription("Very good description for RadarUser");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "title");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailOnEmptyTitle() {
    final RadarUser technology = new RadarUser();
    technology.setTitle("");
    technology.setDescription("Very good description for RadarUser");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "title");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 64"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceTitle() {
    final RadarUser technology = new RadarUser();
    technology.setTitle(" ");
    technology.setDescription("Very good description for RadarUser");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "title");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("should be without whitespaces before and after"));
    }
  }

  @Test
  void shouldFailOnNullDescription() {
    final RadarUser technology = new RadarUser();
    technology.setTitle("TEST");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "description");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailOnEmptyDescription() {
    final RadarUser technology = new RadarUser();
    technology.setTitle("TEST");
    technology.setDescription("");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "description");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 512"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceDescription() {
    final RadarUser technology = new RadarUser();
    technology.setTitle("TEST");
    technology.setDescription(" ");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> technologyRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "description");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailToSaveRadarUserDueToTitleWithRightWhiteSpace() {
    final RadarUser technology = new RadarUser();
    technology.setTitle("My new test RadarUser ");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> technologyRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveRadarUserDueToTitleWithLeftWhiteSpace() {
    final RadarUser technology = new RadarUser();
    technology.setTitle(" My new test RadarUser");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> technologyRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }
}
