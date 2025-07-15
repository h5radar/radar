package com.h5radar.radar.domain;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
// import org.springframework.context.i18n.LocaleContextHolder;

import com.h5radar.radar.ModelApprover;
import com.h5radar.radar.ModelError;
import com.h5radar.radar.ValidationException;
import com.h5radar.radar.technology.Technology;

@RequiredArgsConstructor
public class DomainActiveSaveApprover implements ModelApprover {

  private final MessageSource messageSource;

  private final Technology technology;

  private final Optional<Domain> domainOptional;

  @Override
  public List<ModelError> approve() throws ValidationException {
    /* TODO:
    if (technology.isActive() || domainOptional.isPresent() && radar.getId() != domainOptional.get().getRadar().getId()
        && domainOptional.get().getRadar().isActive()) {
      return List.of(new ModelError("unable_to_save_due_to_active_radar",
          messageSource.getMessage("domain.error.unable_to_save_due_to_active_radar", null,
              LocaleContextHolder.getLocale()), null));
    }
     */
    return new LinkedList<>();
  }
}
