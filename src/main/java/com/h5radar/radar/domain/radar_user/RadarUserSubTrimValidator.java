package com.h5radar.radar.domain.radar_user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class RadarUserSubTrimValidator implements ConstraintValidator<RadarUserTrimSubConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.length() == value.trim().length();
  }
}
