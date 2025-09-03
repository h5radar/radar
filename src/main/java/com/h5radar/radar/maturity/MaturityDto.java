package com.h5radar.radar.maturity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.h5radar.radar.radar_user.RadarUserDto;
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
@JsonPropertyOrder({"id", "radar_user", "title", "description", "position", "color" })
public class MaturityDto {

  private Long id;

  @JsonProperty("radar_user")
  private RadarUserDto radarUserDto;

  private String title;

  private String description;

  private int position;

  private String color;
}
