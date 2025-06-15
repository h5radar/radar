package com.h5radar.radar.domain.practice;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PracticeTitleTrimValidator implements ConstraintValidator<PracticeTrimTitleConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.length() == value.trim().length();
  }
}
