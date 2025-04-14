package com.t9radar.radar.domain.segment;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.t9radar.radar.domain.ModelApprover;
import com.t9radar.radar.domain.ModelError;
import com.t9radar.radar.domain.ValidationException;
import com.t9radar.radar.domain.radar.Radar;

@RequiredArgsConstructor
public class RadarActiveSaveApprover implements ModelApprover {

  private final MessageSource messageSource;

  private final Radar radar;

  private final Optional<Segment> segmentOptional;

  @Override
  public List<ModelError> approve() throws ValidationException {
    if (radar.isActive() || segmentOptional.isPresent() && radar.getId() != segmentOptional.get().getRadar().getId()
        && segmentOptional.get().getRadar().isActive()) {
      return List.of(new ModelError("unable_to_save_due_to_active_radar",
          messageSource.getMessage("segment.error.unable_to_save_due_to_active_radar", null,
              LocaleContextHolder.getLocale()), null));
    }
    return new LinkedList<>();
  }
}
