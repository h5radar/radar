package com.h5radar.radar.domain.product;

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

class ProductRepositoryTests extends AbstractRepositoryTests {
  @Autowired
  private RadarUserRepository radarUserRepository;

  @Autowired
  private ProductRepository productRepository;

  @Test
  void shouldSaveProductWithAllFields() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setTitle("My title");
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    productRepository.saveAndFlush(product);
    Assertions.assertNotNull(product.getId());
    Assertions.assertNotNull(product.getRadarUser());
    Assertions.assertNotNull(product.getCreatedBy());
    Assertions.assertNotNull(product.getCreatedDate());
    Assertions.assertNotNull(product.getLastModifiedBy());
    Assertions.assertNotNull(product.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedProductById() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setTitle("MY");
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    productRepository.saveAndFlush(product);
    Assertions.assertNotNull(product.getId());
    Assertions.assertTrue(productRepository.findById(product.getId()).isPresent());
  }

  @Test
  void shouldFailOnNullTitle() {
    // Create a radar user
    final RadarUser radarUser = new RadarUser();
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");
    radarUserRepository.saveAndFlush(radarUser);

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setTitle("");
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setTitle(" ");
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setTitle("My title");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setTitle("My title");
    product.setDescription("");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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

    final Product product = new Product();
    product.setRadarUser(radarUser);
    product.setTitle("My title");
    product.setDescription(" ");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(1, exception.getConstraintViolations().size());
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals("description", constraintViolation.getPropertyPath().toString());
      Assertions.assertEquals("must not be blank", constraintViolation.getMessage());
    }
  }

  @Test
  void shouldFailToSaveProductDueToTitleWithRightWhiteSpace() {
    final Product product = new Product();
    product.setTitle("My title ");

    Assertions.assertNull(product.getId());
    assertThatThrownBy(() -> productRepository.saveAndFlush(product))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveProductDueToTitleWithLeftWhiteSpace() {
    final Product product = new Product();
    product.setTitle(" My title");

    Assertions.assertNull(product.getId());
    assertThatThrownBy(() -> productRepository.saveAndFlush(product))
        .isInstanceOf(ValidationException.class);
  }
}
