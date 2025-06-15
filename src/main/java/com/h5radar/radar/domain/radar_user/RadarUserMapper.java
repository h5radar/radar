package com.h5radar.radar.domain.radar_user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.license.LicenseMapper;
import com.h5radar.radar.domain.practice.PracticeMapper;
import com.h5radar.radar.domain.technology.TechnologyMapper;

@Mapper(config = MapperConfiguration.class,
    uses = {LicenseMapper.class, PracticeMapper.class, TechnologyMapper.class})
public abstract class RadarUserMapper implements PlainMapper<RadarUser, RadarUserDto> {

  @Mapping(source = "licenseList", target = "licenseDtoList")
  @Mapping(source = "practiceList", target = "practiceDtoList")
  @Mapping(source = "technologyList", target = "technologyDtoList")
  public abstract RadarUserDto toDto(final RadarUser entity);

  @Mapping(source = "licenseDtoList", target = "licenseList")
  @Mapping(source = "practiceDtoList", target = "practiceList")
  @Mapping(source = "technologyDtoList", target = "technologyList")
  public abstract RadarUser toEntity(final RadarUserDto dto);
}
