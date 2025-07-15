package com.h5radar.radar.maturity;


import java.util.LinkedList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
// import org.springframework.context.i18n.LocaleContextHolder;

import com.h5radar.radar.ModelApprover;
import com.h5radar.radar.ModelError;
import com.h5radar.radar.ValidationException;

@RequiredArgsConstructor
public class MaturityActiveDeleteApprover implements ModelApprover {

  private final MessageSource messageSource;

  private final Maturity maturity;

  @Override
  public List<ModelError> approve() throws ValidationException {
    /* TODO: uncomment
    if (maturity.getRadar().isActive()) {
      return List.of(new ModelError("unable_to_delete_due_to_active_radar",
          messageSource.getMessage("maturity.error.unable_to_delete_due_to_active_radar", null,
              LocaleContextHolder.getLocale()), null));
    }
     */
    return new LinkedList<>();
  }
}
