package com.h5radar.radar.practice;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.AbstractRepositoryTests;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;

class PracticeRepositoryTests extends AbstractRepositoryTests {
  @Autowired
  private RadarUserRepository radarUserRepository;

  @Autowired
  private PracticeRepository practiceRepository;

  @Test
  void shouldSavePracticeWithAllFields() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setTitle("My title");
    practice.setDescription("My description");

    Assertions.assertNull(practice.getId());
    practiceRepository.saveAndFlush(practice);
    Assertions.assertNotNull(practice.getId());
    Assertions.assertNotNull(practice.getRadarUser());
    Assertions.assertNotNull(practice.getTitle());
    Assertions.assertNotNull(practice.getDescription());
    Assertions.assertNotNull(practice.getCreatedBy());
    Assertions.assertNotNull(practice.getCreatedDate());
    Assertions.assertNotNull(practice.getLastModifiedBy());
    Assertions.assertNotNull(practice.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedPracticeById() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setTitle("My title");
    practice.setDescription("My description");

    Assertions.assertNull(practice.getId());
    practiceRepository.saveAndFlush(practice);
    Assertions.assertNotNull(practice.getId());
    Assertions.assertTrue(practiceRepository.findById(practice.getId()).isPresent());
  }

  @Test
  void shouldFailOnNullRadarUser() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create practice
    final Practice practice = new Practice();
    practice.setTitle("My title");
    practice.setDescription("My description");

    Assertions.assertNull(practice.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> practiceRepository.saveAndFlush(practice),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("radarUser", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be null", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailOnNullTitle() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setDescription("My description");

    Assertions.assertNull(practice.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> practiceRepository.saveAndFlush(practice),
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

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setTitle("");
    practice.setDescription("My description");

    Assertions.assertNull(practice.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> practiceRepository.saveAndFlush(practice),
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

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setTitle(" ");
    practice.setDescription("My description");

    Assertions.assertNull(practice.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> practiceRepository.saveAndFlush(practice),
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

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setTitle("My title");

    Assertions.assertNull(practice.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> practiceRepository.saveAndFlush(practice),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("description", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be blank", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailOnEmptyDescription() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setTitle("My title");
    practice.setDescription("");

    Assertions.assertNull(practice.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> practiceRepository.saveAndFlush(practice),
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

    // Create practice
    final Practice practice = new Practice();
    practice.setRadarUser(radarUser);
    practice.setTitle("My title");
    practice.setDescription(" ");

    Assertions.assertNull(practice.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> practiceRepository.saveAndFlush(practice),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("description", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be blank", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailToSavePracticeDueToTitleWithRightWhiteSpace() {
    final Practice practice = new Practice();
    practice.setTitle("My title with right white space");

    Assertions.assertNull(practice.getId());
    assertThatThrownBy(() -> practiceRepository.saveAndFlush(practice))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSavePracticeDueToTitleWithLeftWhiteSpace() {
    final Practice practice = new Practice();
    practice.setTitle(" My title with left white space");

    Assertions.assertNull(practice.getId());
    assertThatThrownBy(() -> practiceRepository.saveAndFlush(practice))
        .isInstanceOf(ValidationException.class);
  }
}
