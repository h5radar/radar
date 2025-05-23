package com.h5radar.radar.domain.radar;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.h5radar.radar.domain.ring.RingDto;
import com.h5radar.radar.domain.segment.SegmentDto;
import com.h5radar.radar.domain.technology_blip.TechnologyBlipDto;

/**
 * This class should not have any validation such as @NotNull etc
 * due to custom primary validation at service layer.
 */
@JsonPropertyOrder({"id", "radar_type_id", "title", "description", "primary", "active"})
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RadarDto {

  private Long id;

  @JsonProperty("radar_type_id")
  @JsonIdentityReference(alwaysAsId = true)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private Long radarTypeId;

  private String radarTypeTitle;

  private String title;

  private String description;

  private boolean primary;

  private boolean active = true;

  private List<RingDto>  ringDtoList;

  private List<SegmentDto> segmentDtoList;

  private List<TechnologyBlipDto> technologyBlipDtoList;
}
