package com.h5radar.radar.domain.radar_user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.h5radar.radar.domain.compliance.ComplianceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.h5radar.radar.domain.license.LicenseDto;
import com.h5radar.radar.domain.practice.PracticeDto;
import com.h5radar.radar.domain.product.ProductDto;
import com.h5radar.radar.domain.technology.TechnologyDto;

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
  public RadarUserDto(Long id) {
    this.id = id;
  }

  public RadarUserDto(Long id, String sub, String username) {
    this.id = id;
    this.sub = sub;
    this.username = username;
  }

  private Long id;

  private String sub;

  private String username;

  @JsonIgnore
  private List<ComplianceDto> complianceDtoList;

  @JsonIgnore
  private List<LicenseDto> licenseDtoList;

  @JsonIgnore
  private List<PracticeDto> practiceDtoList;

  @JsonIgnore
  private List<TechnologyDto> technologyDtoList;

  @JsonIgnore
  private List<ProductDto> productDtoList;
}
