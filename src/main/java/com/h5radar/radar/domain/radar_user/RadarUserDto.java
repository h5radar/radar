package com.h5radar.radar.domain.radar_user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.h5radar.radar.domain.ring.RingDto;
import com.h5radar.radar.domain.technology.TechnologyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * This class should not have any validation such as @NotNull etc
 * due to custom primary validation at service layer.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "sub", "username" })
public class RadarUserDto {

  private Long id;

  private String sub;

  private String username;

  private List<TechnologyDto> technologyDtoList;

}
