package com.h5radar.radar.domain.radar_user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RadarUserUsernameTrimValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RadarUserTrimUsernameConstraint {
  String message() default "should be without whitespaces before and after";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
