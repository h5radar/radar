package com.h5radar.radar.technology;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.PlainMapper;
import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;


@Mapper(config = MapperConfiguration.class)
public abstract class TechnologyMapper implements PlainMapper<Technology, TechnologyDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserDto.id")
  @Mapping(source = "radarUser.sub", target = "radarUserDto.sub")
  @Mapping(source = "radarUser.username", target = "radarUserDto.username")
  @Mapping(target = "radarUserDto.complianceDtoList", ignore = true)
  @Mapping(target = "radarUserDto.domainDtoList", ignore = true)
  @Mapping(target = "radarUserDto.maturityDtoList", ignore = true)
  @Mapping(target = "radarUserDto.licenseDtoList", ignore = true)
  @Mapping(target = "radarUserDto.practiceDtoList", ignore = true)
  @Mapping(target = "radarUserDto.technologyDtoList", ignore = true)
  @Mapping(target = "radarUserDto.productDtoList", ignore = true)
  public abstract TechnologyDto toDto(final Technology entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  public abstract Technology toEntity(final TechnologyDto dto);

  RadarUser getRadarUser(TechnologyDto technologyDto) {
    if (technologyDto.getRadarUserDto() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(technologyDto.getRadarUserDto().getId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }
}
