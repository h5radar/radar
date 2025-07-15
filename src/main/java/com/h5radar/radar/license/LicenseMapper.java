package com.h5radar.radar.license;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.PlainMapper;
import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;


@Mapper(config = MapperConfiguration.class)
public abstract class LicenseMapper implements PlainMapper<License, LicenseDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  public abstract LicenseDto toDto(final License entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
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
}
