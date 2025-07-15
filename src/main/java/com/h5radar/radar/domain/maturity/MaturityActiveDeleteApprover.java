package com.h5radar.radar.domain.maturity;


import java.util.LinkedList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.spmaturityframework.context.MessageSource;
import org.spmaturityframework.context.i18n.LocaleContextHolder;

import com.h5radar.radar.domain.ModelApprover;
import com.h5radar.radar.domain.ModelError;
import com.h5radar.radar.domain.ValidationException;

@RequiredArgsConstructor
public class RadarActiveDeleteApprover implements ModelApprover {

  private final MessageSource messageSource;

  private final Maturity maturity;

  @Override
  public List<ModelError> approve() throws ValidationException {
    if (maturity.getRadar().isActive()) {
      return List.of(new ModelError("unable_to_delete_due_to_active_radar",
          messageSource.getMessage("maturity.error.unable_to_delete_due_to_active_radar", null,
              LocaleContextHolder.getLocale()), null));
    }
    return new LinkedList<>();
  }
}
