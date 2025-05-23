package com.h5radar.radar.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FlashMessages {
  public static final String INFO = "msg_info";
  public static final String ERROR = "msg_error";
  public static final String WARNING = "msg_warning";
}
