package com.h5radar.radar.domain;


import java.util.LinkedList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
// import org.springframework.context.i18n.LocaleContextHolder;

import com.h5radar.radar.ModelApprover;
import com.h5radar.radar.ModelError;
import com.h5radar.radar.ValidationException;

@RequiredArgsConstructor
public class DomainActiveDeleteApprover implements ModelApprover {

  private final MessageSource messageSource;

  private final Domain domain;

  @Override
  public List<ModelError> approve() throws ValidationException {
    /* TODO: uncomment
    if (domain.getRadar().isActive()) {
      return List.of(new ModelError("unable_to_delete_due_to_active_radar",
          messageSource.getMessage("domain.error.unable_to_delete_due_to_active_radar", null,
              LocaleContextHolder.getLocale()), null));
    }
     */
    return new LinkedList<>();
  }
}
