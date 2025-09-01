package com.h5radar.radar.compliance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.h5radar.radar.license.LicenseDto;

/**
 * This class should not have any validation such as @NotNull etc
 * due to custom primary validation at service layer.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "radar_user_id", "title", "description", "active" })
public class ComplianceDto {

  private Long id;

  @JsonProperty("radar_user_id")
  @JsonIdentityReference(alwaysAsId = true)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private Long radarUserId;

  private String title;

  private String description;

  private boolean active;

  @JsonIgnore
  private List<LicenseDto> licenseDtoList;
}
