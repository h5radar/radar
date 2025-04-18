package com.h5radar.radar.domain.tenant;

import org.mapstruct.Mapper;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface TenantMapper extends PlainMapper<Tenant, TenantDto> {
}
