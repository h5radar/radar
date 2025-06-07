package com.h5radar.radar.domain.radar_user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"id", "title", "website", "description", "moved", "active" })
public class RadarUserDto {

  private Long id;

  private String title;

  private String website;

  private String description;

  private int moved;

  private boolean active;

}
