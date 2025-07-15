package com.h5radar.radar.domain.radar_user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.compliance.ComplianceMapper;
import com.h5radar.radar.domain.license.LicenseMapper;
import com.h5radar.radar.domain.practice.PracticeMapper;
import com.h5radar.radar.domain.product.ProductMapper;
import com.h5radar.radar.domain.technology.TechnologyMapper;

@Mapper(config = MapperConfiguration.class,
    uses = {ComplianceMapper.class, LicenseMapper.class, PracticeMapper.class,
        ProductMapper.class, TechnologyMapper.class})
public abstract class RadarUserMapper implements PlainMapper<RadarUser, RadarUserDto> {

  @Mapping(source = "complianceList", target = "complianceDtoList")
  @Mapping(source = "licenseList", target = "licenseDtoList")
  @Mapping(source = "practiceList", target = "practiceDtoList")
  @Mapping(source = "technologyList", target = "technologyDtoList")
  @Mapping(source = "productList", target = "productDtoList")
  public abstract RadarUserDto toDto(final RadarUser entity);

  @Mapping(source = "complianceDtoList", target = "complianceList")
  @Mapping(source = "licenseDtoList", target = "licenseList")
  @Mapping(source = "practiceDtoList", target = "practiceList")
  @Mapping(source = "technologyDtoList", target = "technologyList")
  @Mapping(source = "productDtoList", target = "productList")
  public abstract RadarUser toEntity(final RadarUserDto dto);
}
