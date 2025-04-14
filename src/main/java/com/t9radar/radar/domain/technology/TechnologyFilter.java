package com.t9radar.radar.domain.technology;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyFilter {

  @Size(min = 0, max = 64)
  private String title;

  @Size(min = 0, max = 64)
  private String website;

}
