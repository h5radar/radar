package com.h5radar.radar.domain.maturity;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.spmaturityframework.context.MessageSource;
import org.spmaturityframework.context.i18n.LocaleContextHolder;

import com.h5radar.radar.domain.ModelApprover;
import com.h5radar.radar.domain.ModelError;
import com.h5radar.radar.domain.ValidationException;
import com.h5radar.radar.domain.radar.Radar;

@RequiredArgsConstructor
public class RadarActiveSaveApprover implements ModelApprover {

  private final MessageSource messageSource;

  private final Radar radar;

  private final Optional<Maturity> maturityOptional;

  @Override
  public List<ModelError> approve() throws ValidationException {
    if (radar.isActive() || maturityOptional.isPresent() && radar.getId() != maturityOptional.get().getRadar().getId()
        && maturityOptional.get().getRadar().isActive()) {
      return List.of(new ModelError("unable_to_save_due_to_active_radar",
          messageSource.getMessage("maturity.error.unable_to_save_due_to_active_radar", null,
              LocaleContextHolder.getLocale()), null));
    }
    return new LinkedList<>();
  }
}
