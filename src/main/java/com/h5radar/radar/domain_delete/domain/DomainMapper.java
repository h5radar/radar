package com.h5radar.radar.domain.domain;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;

@Mapper(config = MapperConfiguration.class)
public abstract class DomainMapper implements PlainMapper<Domain, DomainDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  public abstract DomainDto toDto(final Domain entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  public abstract Domain toEntity(final DomainDto dto);

  RadarUser getRadarUser(DomainDto domainDto) {
    if (domainDto.getRadarUserId() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(domainDto.getRadarUserId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }
}
