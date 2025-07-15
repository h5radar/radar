package com.h5radar.radar.radar_user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.h5radar.radar.compliance.ComplianceDto;
import com.h5radar.radar.domain.DomainDto;
import com.h5radar.radar.license.LicenseDto;
import com.h5radar.radar.maturity.MaturityDto;
import com.h5radar.radar.practice.PracticeDto;
import com.h5radar.radar.product.ProductDto;
import com.h5radar.radar.technology.TechnologyDto;

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
  private List<DomainDto> domainDtoList;

  @JsonIgnore
  private List<MaturityDto> maturityDtoList;

  @JsonIgnore
  private List<LicenseDto> licenseDtoList;

  @JsonIgnore
  private List<PracticeDto> practiceDtoList;

  @JsonIgnore
  private List<TechnologyDto> technologyDtoList;

  @JsonIgnore
  private List<ProductDto> productDtoList;
}
