package com.h5radar.radar.license;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.PlainMapper;
import com.h5radar.radar.compliance.Compliance;
import com.h5radar.radar.compliance.ComplianceRepository;
import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;



@Mapper(config = MapperConfiguration.class)
public abstract class LicenseMapper implements PlainMapper<License, LicenseDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Autowired
  protected ComplianceRepository complianceRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  @Mapping(source = "compliance.id", target = "complianceDto.id")
  @Mapping(source = "compliance.radarUser.id", target = "complianceDto.radarUserId")
  @Mapping(source = "compliance.title", target = "complianceDto.title")
  @Mapping(source = "compliance.active", target = "complianceDto.active")
  @Mapping(target = "complianceDto.licenseDtoList", ignore = true)
  public abstract LicenseDto toDto(final License entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  @Mapping(target = "compliance", expression = "java(getCompliance(dto))")
  public abstract License toEntity(final LicenseDto dto);

  RadarUser getRadarUser(LicenseDto licenseDto) {
    if (licenseDto.getRadarUserId() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(licenseDto.getRadarUserId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }

  Compliance getCompliance(LicenseDto licenseDto) {
    if (licenseDto.getComplianceDto() != null) {
      Optional<Compliance> complianceOptional = complianceRepository.findById(licenseDto.getComplianceDto().getId());
      if (complianceOptional.isPresent()) {
        return complianceOptional.get();
      }
    }
    return null;
  }

}
