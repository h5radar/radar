package com.h5radar.radar.radar_user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.h5radar.radar.PlainMapper;
import com.h5radar.radar.compliance.ComplianceMapper;
import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.DomainMapper;
import com.h5radar.radar.license.LicenseMapper;
import com.h5radar.radar.maturity.MaturityMapper;
import com.h5radar.radar.practice.PracticeMapper;
import com.h5radar.radar.product.ProductMapper;
import com.h5radar.radar.technology.TechnologyMapper;

@Mapper(config = MapperConfiguration.class,
    uses = {ComplianceMapper.class, DomainMapper.class, LicenseMapper.class, MaturityMapper.class,
        PracticeMapper.class, ProductMapper.class, TechnologyMapper.class})
public abstract class RadarUserMapper implements PlainMapper<RadarUser, RadarUserDto> {

  @Mapping(source = "complianceList", target = "complianceDtoList")
  @Mapping(source = "domainList", target = "domainDtoList")
  @Mapping(source = "maturityList", target = "maturityDtoList")
  @Mapping(source = "licenseList", target = "licenseDtoList")
  @Mapping(source = "practiceList", target = "practiceDtoList")
  @Mapping(source = "technologyList", target = "technologyDtoList")
  @Mapping(source = "productList", target = "productDtoList")
  public abstract RadarUserDto toDto(final RadarUser entity);

  @Mapping(source = "complianceDtoList", target = "complianceList")
  @Mapping(source = "domainDtoList", target = "domainList")
  @Mapping(source = "maturityDtoList", target = "maturityList")
  @Mapping(source = "licenseDtoList", target = "licenseList")
  @Mapping(source = "practiceDtoList", target = "practiceList")
  @Mapping(source = "technologyDtoList", target = "technologyList")
  @Mapping(source = "productDtoList", target = "productList")
  public abstract RadarUser toEntity(final RadarUserDto dto);
}
