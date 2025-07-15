package com.h5radar.radar.compliance;

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

class ComplianceRepositoryTests extends AbstractRepositoryTests {
  @Autowired
  private RadarUserRepository radarUserRepository;

  @Autowired
  private ComplianceRepository complianceRepository;

  @Test
  void shouldSaveComplianceWithAllFields() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");

    Assertions.assertNull(compliance.getId());
    complianceRepository.saveAndFlush(compliance);
    Assertions.assertNotNull(compliance.getId());
    Assertions.assertNotNull(compliance.getRadarUser());
    Assertions.assertNotNull(compliance.getTitle());
    Assertions.assertNotNull(compliance.getDescription());
    Assertions.assertNotNull(compliance.getCreatedBy());
    Assertions.assertNotNull(compliance.getCreatedDate());
    Assertions.assertNotNull(compliance.getLastModifiedBy());
    Assertions.assertNotNull(compliance.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedComplianceById() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");

    Assertions.assertNull(compliance.getId());
    complianceRepository.saveAndFlush(compliance);
    Assertions.assertNotNull(compliance.getId());
    Assertions.assertTrue(complianceRepository.findById(compliance.getId()).isPresent());
  }

  @Test
  void shouldFailOnNullTitle() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setDescription("My description");

    Assertions.assertNull(compliance.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> complianceRepository.saveAndFlush(compliance),
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

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("");
    compliance.setDescription("My description");

    Assertions.assertNull(compliance.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> complianceRepository.saveAndFlush(compliance),
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

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle(" ");
    compliance.setDescription("My description");

    Assertions.assertNull(compliance.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> complianceRepository.saveAndFlush(compliance),
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

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");

    Assertions.assertNull(compliance.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> complianceRepository.saveAndFlush(compliance),
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

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("");

    Assertions.assertNull(compliance.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> complianceRepository.saveAndFlush(compliance),
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

    // Create compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription(" ");

    Assertions.assertNull(compliance.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> complianceRepository.saveAndFlush(compliance),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("description", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be blank", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailToSaveComplianceDueToTitleWithRightWhiteSpace() {
    final Compliance compliance = new Compliance();
    compliance.setTitle("My title with right white space");

    Assertions.assertNull(compliance.getId());
    assertThatThrownBy(() -> complianceRepository.saveAndFlush(compliance))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveComplianceDueToTitleWithLeftWhiteSpace() {
    final Compliance compliance = new Compliance();
    compliance.setTitle(" My title with left white space");

    Assertions.assertNull(compliance.getId());
    assertThatThrownBy(() -> complianceRepository.saveAndFlush(compliance))
        .isInstanceOf(ValidationException.class);
  }
}
