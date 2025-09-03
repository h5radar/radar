package com.h5radar.radar.license;

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

import com.h5radar.radar.compliance.ComplianceDto;

/**
 * This class should not have any validation such as @NotNull etc
 * due to custom primary validation at service layer.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "radar_user", "title", "description", "compliance", "active" })
public class LicenseDto {

  private Long id;

  @JsonProperty("radar_user")
  private RadarUserDto radarUserDto;

  private String title;

  private String description;

  @JsonProperty("compliance")
  private ComplianceDto complianceDto;

  private boolean active;

}
