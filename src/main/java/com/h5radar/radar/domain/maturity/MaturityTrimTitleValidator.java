package com.h5radar.radar.domain.maturity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class MaturityTrimTitleValidator implements ConstraintValidator<MaturityTrimTitleConstraint, Stmaturity> {

  @Override
  public boolean isValid(Stmaturity value, ConstraintValidatorContext context) {
    return value == null || value.length() == value.trim().length();
  }
}
