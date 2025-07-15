package com.h5radar.radar.domain.maturity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StmaturityUtils;


public class MaturityUppercaseTitleValidator implements ConstraintValidator<MaturityUppercaseTitleConstraint, Stmaturity> {

  @Override
  public boolean isValid(Stmaturity value, ConstraintValidatorContext context) {
    return StmaturityUtils.isAllUpperCase(value);
  }
}
