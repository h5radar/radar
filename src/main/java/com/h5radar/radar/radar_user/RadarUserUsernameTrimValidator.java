package com.h5radar.radar.radar_user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class RadarUserUsernameTrimValidator implements ConstraintValidator<RadarUserTrimUsernameConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.length() == value.trim().length();
  }
}
