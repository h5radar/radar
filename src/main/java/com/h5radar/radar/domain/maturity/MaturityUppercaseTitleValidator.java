package com.h5radar.radar.domain.ring;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;


public class RingUppercaseTitleValidator implements ConstraintValidator<RingUppercaseTitleConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isAllUpperCase(value);
  }
}
