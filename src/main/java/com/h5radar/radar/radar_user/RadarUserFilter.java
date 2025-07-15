package com.h5radar.radar.radar_user;

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
public class RadarUserFilter {

  @Size(min = 0, max = 255)
  private String sub;

  @Size(min = 0, max = 255)
  private String username;

}
