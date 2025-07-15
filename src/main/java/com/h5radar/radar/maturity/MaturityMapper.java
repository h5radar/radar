package com.h5radar.radar.maturity;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.PlainMapper;
import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;


@Mapper(config = MapperConfiguration.class)
public abstract class MaturityMapper implements PlainMapper<Maturity, MaturityDto> {

  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  public abstract MaturityDto toDto(final Maturity entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  public abstract Maturity toEntity(final MaturityDto dto);

  RadarUser getRadarUser(MaturityDto maturityDto) {
    if (maturityDto.getRadarUserId() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(maturityDto.getRadarUserId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }
}
