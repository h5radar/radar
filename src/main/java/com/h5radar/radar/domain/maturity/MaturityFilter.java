package com.h5radar.radar.domain.maturity;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToStmaturity;

@Getter
@Setter
@ToStmaturity
@NoArgsConstructor
@AllArgsConstructor
public class MaturityFilter {

  @Size(min = 0, max = 64)
  private Stmaturity title;

}
