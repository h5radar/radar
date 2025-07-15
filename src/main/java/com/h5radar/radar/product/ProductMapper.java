package com.h5radar.radar.domain.product;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;


@Mapper(config = MapperConfiguration.class)
public abstract class ProductMapper implements PlainMapper<Product, ProductDto> {
  @Autowired
  protected RadarUserRepository radarUserRepository;

  @Mapping(source = "radarUser.id", target = "radarUserId")
  public abstract ProductDto toDto(final Product entity);

  @Mapping(target = "radarUser", expression = "java(getRadarUser(dto))")
  public abstract Product toEntity(final ProductDto dto);

  RadarUser getRadarUser(ProductDto productDto) {
    if (productDto.getRadarUserId() != null) {
      Optional<RadarUser> radarUserOptional = radarUserRepository.findById(productDto.getRadarUserId());
      if (radarUserOptional.isPresent()) {
        return radarUserOptional.get();
      }
    }
    return null;
  }
}
