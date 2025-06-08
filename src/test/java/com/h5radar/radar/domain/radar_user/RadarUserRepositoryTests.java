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
  private RadarUserRepository radarUserRepository;

  @Test
  void shouldSaveRadarUserWithAllFields() {
    final RadarUser technology = new RadarUser();
    technology.setSub("My sub");
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    radarUserRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    Assertions.assertNotNull(technology.getCreatedBy());
    Assertions.assertNotNull(technology.getCreatedDate());
    Assertions.assertNotNull(technology.getLastModifiedBy());
    Assertions.assertNotNull(technology.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedRadarUserById() {
    final RadarUser technology = new RadarUser();
    technology.setSub("MY");
    technology.setUsername("Very good username for RadarUser");

    Assertions.assertNull(technology.getId());
    radarUserRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    var id = technology.getId();

    Assertions.assertTrue(radarUserRepository.findById(id).isPresent());
  }

  @Test
  void shouldFailOnNullSub() {
    final RadarUser technology = new RadarUser();
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "sub");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailOnEmptySub() {
    final RadarUser technology = new RadarUser();
    technology.setSub("");
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "sub");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 255"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceSub() {
    final RadarUser technology = new RadarUser();
    technology.setSub(" ");
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "sub");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("should be without whitespaces before and after"));
    }
  }

  @Test
  void shouldFailOnNullUsername() {
    final RadarUser technology = new RadarUser();
    technology.setSub("My sub");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "username");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailOnEmptyUsername() {
    final RadarUser technology = new RadarUser();
    technology.setSub("My sub");
    technology.setUsername("");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "username");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 255"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceUsername() {
    final RadarUser technology = new RadarUser();
    technology.setSub("My sub");
    technology.setUsername(" ");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "username");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("should be without whitespaces before and after"));
    }
  }

  @Test
  void shouldFailToSaveRadarUserDueToSubWithRightWhiteSpace() {
    final RadarUser technology = new RadarUser();
    technology.setSub("My new test RadarUser ");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> radarUserRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveRadarUserDueToSubWithLeftWhiteSpace() {
    final RadarUser technology = new RadarUser();
    technology.setSub(" My new test RadarUser");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> radarUserRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }
}
