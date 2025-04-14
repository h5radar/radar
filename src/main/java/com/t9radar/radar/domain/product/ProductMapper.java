package com.t9radar.radar.domain.product;

import org.mapstruct.Mapper;

import com.t9radar.radar.config.MapperConfiguration;
import com.t9radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface ProductMapper extends PlainMapper<Product, ProductDto> {
}
