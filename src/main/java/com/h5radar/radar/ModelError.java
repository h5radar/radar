package com.h5radar.radar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelError {

  private String errorCode;

  private String errorMessage;

  private String field;
}
