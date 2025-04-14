package com.t9radar.radar.domain.tenant;

import org.mapstruct.Mapper;

import com.t9radar.radar.config.MapperConfiguration;
import com.t9radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface TenantMapper extends PlainMapper<Tenant, TenantDto> {
}
