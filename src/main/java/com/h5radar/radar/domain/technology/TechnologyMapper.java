package com.h5radar.radar.domain.technology;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;


@Mapper(config = MapperConfiguration.class)
public abstract class TechnologyMapper implements PlainMapper<Technology, TechnologyDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  public abstract TechnologyDto toDto(final Technology entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  public abstract Technology toEntity(final TechnologyDto dto);

  RadarUser getRadarUser(TechnologyDto technologyDto) {
    if (technologyDto.getRadarUserId() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(technologyDto.getRadarUserId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }
}
