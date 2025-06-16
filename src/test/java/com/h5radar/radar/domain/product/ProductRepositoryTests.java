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

class ProductRepositoryTests extends AbstractRepositoryTests {

  @Autowired
  private ProductRepository productRepository;

  @Test
  void shouldSaveProductWithAllFields() {
    final Product product = new Product();
    product.setTitle("My title");
      product.setDescription("My description");

    Assertions.assertNull(product.getId());
    productRepository.saveAndFlush(product);
    Assertions.assertNotNull(product.getId());
    Assertions.assertNotNull(product.getCreatedBy());
    Assertions.assertNotNull(product.getCreatedDate());
    Assertions.assertNotNull(product.getLastModifiedBy());
    Assertions.assertNotNull(product.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedProductById() {
    final Product product = new Product();
    product.setTitle("MY");
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    productRepository.saveAndFlush(product);
    Assertions.assertNotNull(product.getId());
    var id = product.getId();

    Assertions.assertTrue(productRepository.findById(id).isPresent());
  }

  @Test
  void shouldFailOnNullTitle() {
    final Product product = new Product();
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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
    final Product product = new Product();
    product.setTitle("");
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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
    final Product product = new Product();
    product.setTitle(" ");
    product.setDescription("My description");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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
    final Product product = new Product();
    product.setTitle("My title");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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
    final Product product = new Product();
    product.setTitle("My title");
    product.setDescription("");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
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
    final Product product = new Product();
    product.setTitle("My title");
    product.setDescription(" ");

    Assertions.assertNull(product.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> productRepository.saveAndFlush(product),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "description");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailToSaveProductDueToTitleWithRightWhiteSpace() {
    final Product product = new Product();
    product.setTitle("My new test Product ");

    Assertions.assertNull(product.getId());
    assertThatThrownBy(() -> productRepository.saveAndFlush(product))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveProductDueToTitleWithLeftWhiteSpace() {
    final Product product = new Product();
    product.setTitle(" My new test Product");

    Assertions.assertNull(product.getId());
    assertThatThrownBy(() -> productRepository.saveAndFlush(product))
        .isInstanceOf(ValidationException.class);
  }
}
