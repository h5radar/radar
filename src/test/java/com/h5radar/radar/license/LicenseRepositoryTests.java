package com.h5radar.radar.license;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.AbstractRepositoryTests;
import com.h5radar.radar.compliance.Compliance;
import com.h5radar.radar.compliance.ComplianceRepository;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;

class LicenseRepositoryTests extends AbstractRepositoryTests {
  @Autowired
  private RadarUserRepository radarUserRepository;

  @Autowired
  private ComplianceRepository complianceRepository;

  @Autowired
  private LicenseRepository licenseRepository;

  @Test
  void shouldSaveLicenseWithAllFields() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle("My title");
    license.setDescription("My description");
    license.setCompliance(compliance);

    Assertions.assertNull(license.getId());
    licenseRepository.saveAndFlush(license);
    Assertions.assertNotNull(license.getId());
    Assertions.assertNotNull(license.getRadarUser());
    Assertions.assertNotNull(license.getTitle());
    Assertions.assertNotNull(license.getDescription());
    Assertions.assertNotNull(license.getCompliance());
    Assertions.assertNotNull(license.getCreatedBy());
    Assertions.assertNotNull(license.getCreatedDate());
    Assertions.assertNotNull(license.getLastModifiedBy());
    Assertions.assertNotNull(license.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedLicenseById() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle("My title");
    license.setDescription("My description");
    license.setCompliance(compliance);
    license.setActive(true);

    Assertions.assertNull(license.getId());
    licenseRepository.saveAndFlush(license);
    Assertions.assertNotNull(license.getId());
    Assertions.assertTrue(licenseRepository.findById(license.getId()).isPresent());
  }

  @Test
  void shouldFailOnNullRadarUser() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setTitle("My title");
    license.setDescription("My description");
    license.setCompliance(compliance);
    license.setActive(true);


    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "radarUser");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be null");
    }
  }

  @Test
  void shouldFailOnNullTitle() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setDescription("My description");
    license.setCompliance(compliance);

    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
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

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle("");
    license.setDescription("My description");
    license.setCompliance(compliance);

    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
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

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle(" ");
    license.setDescription("My description");
    license.setCompliance(compliance);

    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
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
  void shouldFailToSaveLicenseDueToTitleWithRightWhiteSpace() {
    final License license = new License();
    license.setTitle("My title with right white space");

    Assertions.assertNull(license.getId());
    assertThatThrownBy(() -> licenseRepository.saveAndFlush(license))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveLicenseDueToTitleWithLeftWhiteSpace() {
    final License license = new License();
    license.setTitle(" My title with left white space");

    Assertions.assertNull(license.getId());
    assertThatThrownBy(() -> licenseRepository.saveAndFlush(license))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailOnNullDescription() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle("My title");
    license.setCompliance(compliance);

    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
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

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle("My title");
    license.setDescription("");
    license.setCompliance(compliance);

    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
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

    // Create a compliance
    final Compliance compliance = new Compliance();
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My title");
    compliance.setDescription("My description");
    compliance.setActive(true);
    complianceRepository.saveAndFlush(compliance);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle("My title");
    license.setDescription(" ");
    license.setCompliance(compliance);

    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("description", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be blank", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailOnNullCompliance() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    // Create license
    final License license = new License();
    license.setRadarUser(radarUser);
    license.setTitle("My title");
    license.setDescription("My description");

    Assertions.assertNull(license.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> licenseRepository.saveAndFlush(license),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "compliance");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be null");
    }
  }
}
