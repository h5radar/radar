package com.h5radar.radar.domain.maturity;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
// import org.springframework.context.i18n.LocaleContextHolder;

import com.h5radar.radar.domain.ModelApprover;
import com.h5radar.radar.domain.ModelError;
import com.h5radar.radar.domain.ValidationException;
import com.h5radar.radar.domain.technology.Technology;

@RequiredArgsConstructor
public class MaturityActiveSaveApprover implements ModelApprover {

  private final MessageSource messageSource;

  private final Technology technology;

  private final Optional<Maturity> maturityOptional;

  @Override
  public List<ModelError> approve() throws ValidationException {
    /* TODO: uncomment
    if (radar.isActive() || maturityOptional.isPresent() && radar.getId() != maturityOptional.get().getRadar().getId()
        && maturityOptional.get().getRadar().isActive()) {
      return List.of(new ModelError("unable_to_save_due_to_active_radar",
          messageSource.getMessage("maturity.error.unable_to_save_due_to_active_radar", null,
              LocaleContextHolder.getLocale()), null));
    }
     */
    return new LinkedList<>();
  }
}
