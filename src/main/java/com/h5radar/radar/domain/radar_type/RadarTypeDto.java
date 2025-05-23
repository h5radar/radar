package com.h5radar.radar.domain.radar_type;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class should not have any validation such as @NotNull etc
 * due to custom primary validation at service layer.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RadarTypeDto {

  private Long id;

  @NotBlank
  private String title;

  @NotBlank
  private String code;

  private String description;
}
