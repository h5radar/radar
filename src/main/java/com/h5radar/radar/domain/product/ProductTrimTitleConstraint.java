package com.h5radar.radar.domain.product;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ProductTitleTrimValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductTrimTitleConstraint {
  String message() default "should be without whitespaces before and after";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}