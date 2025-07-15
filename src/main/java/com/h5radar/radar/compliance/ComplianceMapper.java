package com.h5radar.radar.compliance;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.PlainMapper;
import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;


@Mapper(config = MapperConfiguration.class)
public abstract class ComplianceMapper implements PlainMapper<Compliance, ComplianceDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  public abstract ComplianceDto toDto(final Compliance entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  public abstract Compliance toEntity(final ComplianceDto dto);

  RadarUser getRadarUser(ComplianceDto complianceDto) {
    if (complianceDto.getRadarUserId() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(complianceDto.getRadarUserId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }
}
