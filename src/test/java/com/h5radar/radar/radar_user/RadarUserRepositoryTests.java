package com.h5radar.radar.radar_user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.AbstractRepositoryTests;

class RadarUserRepositoryTests extends AbstractRepositoryTests {

  @Autowired
  private RadarUserRepository radarUserRepository;

  @Test
  void shouldSaveRadarUserWithAllFields() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    Assertions.assertNull(radarUser.getId());
    radarUserRepository.saveAndFlush(radarUser);
    Assertions.assertNotNull(radarUser.getId());
    Assertions.assertNotNull(radarUser.getCreatedBy());
    Assertions.assertNotNull(radarUser.getCreatedDate());
    Assertions.assertNotNull(radarUser.getLastModifiedBy());
    Assertions.assertNotNull(radarUser.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedRadarUserById() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("MY");
    radarUser.setUsername("Very good username for RadarUser");

    Assertions.assertNull(radarUser.getId());
    radarUserRepository.saveAndFlush(radarUser);
    Assertions.assertNotNull(radarUser.getId());
    var id = radarUser.getId();

    Assertions.assertTrue(radarUserRepository.findById(id).isPresent());
  }

  @Test
  void shouldFailOnNullSub() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setUsername("My username");

    Assertions.assertNull(radarUser.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(radarUser),
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
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("");
    radarUser.setUsername("My username");

    Assertions.assertNull(radarUser.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(radarUser),
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
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub(" ");
    radarUser.setUsername("My username");

    Assertions.assertNull(radarUser.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(radarUser),
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
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");

    Assertions.assertNull(radarUser.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(radarUser),
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
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("");

    Assertions.assertNull(radarUser.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(radarUser),
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
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername(" ");

    Assertions.assertNull(radarUser.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> radarUserRepository.saveAndFlush(radarUser),
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
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My new test RadarUser ");

    Assertions.assertNull(radarUser.getId());
    assertThatThrownBy(() -> radarUserRepository.saveAndFlush(radarUser))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveRadarUserDueToSubWithLeftWhiteSpace() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub(" My new test RadarUser");

    Assertions.assertNull(radarUser.getId());
    assertThatThrownBy(() -> radarUserRepository.saveAndFlush(radarUser))
        .isInstanceOf(ValidationException.class);
  }
}
