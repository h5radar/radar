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

  @Test
  void shouldGroupByComplianceRawAggregateCountsForUser() {
    // Create radar user
    final RadarUser radarUser1 = new RadarUser();
    radarUser1.setSub("My sub");
    radarUser1.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser1);

    // Create compliances
    final Compliance complianceHigh = new Compliance();
    complianceHigh.setRadarUser(radarUser1);
    complianceHigh.setTitle("High");
    complianceHigh.setDescription("High compliance");
    complianceHigh.setActive(true);
    complianceRepository.saveAndFlush(complianceHigh);

    final Compliance complianceLow = new Compliance();
    complianceLow.setRadarUser(radarUser1);
    complianceLow.setTitle("Low");
    complianceLow.setDescription("Low compliance");
    complianceLow.setActive(true);
    complianceRepository.saveAndFlush(complianceLow);

    // Create licenses for radarUser1 (High x2, Low x1)
    final License license1 = new License();
    license1.setRadarUser(radarUser1);
    license1.setTitle("L1");
    license1.setDescription("Desc");
    license1.setCompliance(complianceHigh);
    license1.setActive(true);
    licenseRepository.saveAndFlush(license1);

    final License license2 = new License();
    license2.setRadarUser(radarUser1);
    license2.setTitle("L2");
    license2.setDescription("Desc");
    license2.setCompliance(complianceHigh);
    license2.setActive(true);
    licenseRepository.saveAndFlush(license2);

    final License license3 = new License();
    license3.setRadarUser(radarUser1);
    license3.setTitle("L3");
    license3.setDescription("Desc");
    license3.setCompliance(complianceLow);
    license3.setActive(true);
    licenseRepository.saveAndFlush(license3);

    // Act
    final var rows = licenseRepository.groupByComplianceRaw(radarUser1.getId());

    // Assert
    Assertions.assertNotNull(rows);

    final long highCount = rows.stream()
        .filter(r -> complianceHigh.getId().equals((Long) r[0]))
        .mapToLong(r -> ((Number) r[2]).longValue())
        .findFirst()
        .orElse(0L);

    final long lowCount = rows.stream()
        .filter(r -> complianceLow.getId().equals((Long) r[0]))
        .mapToLong(r -> ((Number) r[2]).longValue())
        .findFirst()
        .orElse(0L);

    Assertions.assertEquals(2L, highCount);
    Assertions.assertEquals(1L, lowCount);

    // Titles also match
    Assertions.assertTrue(rows.stream().anyMatch(r ->
        complianceHigh.getId().equals((Long) r[0]) && complianceHigh.getTitle().equals(r[1])));
    Assertions.assertTrue(rows.stream().anyMatch(r ->
        complianceLow.getId().equals((Long) r[0]) && complianceLow.getTitle().equals(r[1])));
  }

  @Test
  void shouldGroupByComplianceRawIsolatedByRadarUser() {
    // Create users
    final RadarUser radarUser1 = new RadarUser();
    radarUser1.setSub("My sub 1");
    radarUser1.setUsername("My username 1");
    radarUserRepository.saveAndFlush(radarUser1);

    final RadarUser radarUser2 = new RadarUser();
    radarUser2.setSub("My sub 2");
    radarUser2.setUsername("My username 2");
    radarUserRepository.saveAndFlush(radarUser2);

    // Compliances per user
    final Compliance compliance1 = new Compliance();
    compliance1.setRadarUser(radarUser1);
    compliance1.setTitle("My title 1");
    compliance1.setDescription("My descriptioin 1");
    compliance1.setActive(true);
    complianceRepository.saveAndFlush(compliance1);

    final Compliance compliance2 = new Compliance();
    compliance2.setRadarUser(radarUser2);
    compliance2.setTitle("My title 2");
    compliance2.setDescription("My description 2");
    compliance2.setActive(true);
    complianceRepository.saveAndFlush(compliance2);

    // Licenses for radarUser1 (2 items)
    final License license1 = new License();
    license1.setRadarUser(radarUser1);
    license1.setTitle("My title 1");
    license1.setDescription("My description 1");
    license1.setCompliance(compliance1);
    license1.setActive(true);
    licenseRepository.saveAndFlush(license1);

    final License license2 = new License();
    license2.setRadarUser(radarUser1);
    license2.setTitle("My title 2");
    license2.setDescription("My description 2");
    license2.setCompliance(compliance1);
    license2.setActive(true);
    licenseRepository.saveAndFlush(license2);

    // License for radarUser2 (noise)
    final License license3 = new License();
    license3.setRadarUser(radarUser2);
    license3.setTitle("My title 3");
    license3.setDescription("My description 3");
    license3.setCompliance(compliance2);
    license3.setActive(true);
    licenseRepository.saveAndFlush(license3);

    // Act
    final var rowsFor1 = licenseRepository.groupByComplianceRaw(radarUser1.getId());
    final var rowsFor2 = licenseRepository.groupByComplianceRaw(radarUser2.getId());

    // Assert A
    final long countFor1 = rowsFor1.stream()
        .filter(r -> compliance1.getId().equals((Long) r[0]))
        .mapToLong(r -> ((Number) r[2]).longValue())
        .findFirst()
        .orElse(0L);
    Assertions.assertEquals(2L, countFor1);

    // Assert B
    final long countFor2 = rowsFor2.stream()
        .filter(r -> compliance2.getId().equals((Long) r[0]))
        .mapToLong(r -> ((Number) r[2]).longValue())
        .findFirst()
        .orElse(0L);
    Assertions.assertEquals(1L, countFor2);
  }

  @Test
  void shouldGroupByComplianceRawReturnEmptyWhenUserHasNoLicenses() {
    // Create user with no licenses
    final RadarUser radarUserEmpty = new RadarUser();
    radarUserEmpty.setSub("My sub");
    radarUserEmpty.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUserEmpty);

    // Act
    final var rows = licenseRepository.groupByComplianceRaw(radarUserEmpty.getId());

    // Assert
    Assertions.assertNotNull(rows);
    Assertions.assertTrue(rows.isEmpty());
  }

}
