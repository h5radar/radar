package com.h5radar.radar.domain.tenant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.domain.AbstractRepositoryTests;

class TenantRepositoryTests extends AbstractRepositoryTests {

  @Autowired
  private TenantRepository tenantRepository;

  @Test
  void shouldSaveTenantWithAllFields() {
    final Tenant tenant = new Tenant();
    tenant.setTitle("TEST");
    tenant.setDescription("Very good description for Tenant");

    Assertions.assertNull(tenant.getId());
    tenantRepository.saveAndFlush(tenant);
    Assertions.assertNotNull(tenant.getId());
    Assertions.assertNotNull(tenant.getCreatedBy());
    Assertions.assertNotNull(tenant.getCreatedDate());
    Assertions.assertNotNull(tenant.getLastModifiedBy());
    Assertions.assertNotNull(tenant.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedTenantById() {
    final Tenant tenant = new Tenant();
    tenant.setTitle("MY");
    tenant.setDescription("Very good description for Tenant");

    Assertions.assertNull(tenant.getId());
    tenantRepository.saveAndFlush(tenant);
    Assertions.assertNotNull(tenant.getId());
    var id = tenant.getId();

    Assertions.assertTrue(tenantRepository.findById(id).isPresent());
  }

  @Test
  void shouldFailOnNullTitle() {
    final Tenant tenant = new Tenant();
    tenant.setDescription("Very good description for Tenant");

    Assertions.assertNull(tenant.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> tenantRepository.saveAndFlush(tenant),
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
    final Tenant tenant = new Tenant();
    tenant.setTitle("");
    tenant.setDescription("Very good description for Tenant");

    Assertions.assertNull(tenant.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> tenantRepository.saveAndFlush(tenant),
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
    final Tenant tenant = new Tenant();
    tenant.setTitle(" ");
    tenant.setDescription("Very good description for Tenant");

    Assertions.assertNull(tenant.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> tenantRepository.saveAndFlush(tenant),
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
    final Tenant tenant = new Tenant();
    tenant.setTitle("TEST");

    Assertions.assertNull(tenant.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> tenantRepository.saveAndFlush(tenant),
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
    final Tenant tenant = new Tenant();
    tenant.setTitle("TEST");
    tenant.setDescription("");

    Assertions.assertNull(tenant.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> tenantRepository.saveAndFlush(tenant),
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
    final Tenant tenant = new Tenant();
    tenant.setTitle("TEST");
    tenant.setDescription(" ");

    Assertions.assertNull(tenant.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> tenantRepository.saveAndFlush(tenant),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "description");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailToSaveTenantDueToTitleWithRightWhiteSpace() {
    final Tenant tenant = new Tenant();
    tenant.setTitle("My new test Tenant ");

    Assertions.assertNull(tenant.getId());
    assertThatThrownBy(() -> tenantRepository.saveAndFlush(tenant))
            .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveTenantDueToTitleWithLeftWhiteSpace() {
    final Tenant tenant = new Tenant();
    tenant.setTitle(" My new test Tenant");

    Assertions.assertNull(tenant.getId());
    assertThatThrownBy(() -> tenantRepository.saveAndFlush(tenant))
            .isInstanceOf(ValidationException.class);
  }
}
