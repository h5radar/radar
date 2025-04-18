package com.h5radar.radar.domain.product;

import org.mapstruct.Mapper;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface ProductMapper extends PlainMapper<Product, ProductDto> {
}
