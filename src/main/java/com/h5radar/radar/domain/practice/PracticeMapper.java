package com.h5radar.radar.domain.practice;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;


@Mapper(config = MapperConfiguration.class)
public abstract class PracticeMapper implements PlainMapper<Practice, PracticeDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  public abstract PracticeDto toDto(final Practice entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  public abstract Practice toEntity(final PracticeDto dto);

  RadarUser getRadarUser(PracticeDto practiceDto) {
    if (practiceDto.getRadarUserId() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(practiceDto.getRadarUserId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }
}
