package com.h5radar.radar.domain.tenant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.radar.domain.AbstractMapperTests;

class TenantMapperTests extends AbstractMapperTests {
  @Autowired
  private TenantMapper tenantMapper;

  @Test
  void testToDtoWithNull() {
    final var tenantDto = tenantMapper.toDto(null);

    Assertions.assertNull(tenantDto);
  }

  @Test
  void testToDtoAllFields() {
    final Tenant tenant = new Tenant(0L, "title", "desciption");
    final var tenantDto = tenantMapper.toDto(tenant);

    Assertions.assertEquals(tenantDto.getTitle(), tenant.getTitle());
    Assertions.assertEquals(tenantDto.getDescription(), tenant.getDescription());
  }

  @Test
  void testToEntityWithNull() {
    final var tenant = tenantMapper.toEntity(null);

    Assertions.assertNull(tenant);
  }

  @Test
  void testToEntityAllFields() {
    final TenantDto tenantDto = new TenantDto(0L, "my title1", "my description1");
    final var tenant = tenantMapper.toEntity(tenantDto);

    Assertions.assertEquals(tenant.getId(), tenantDto.getId());
    Assertions.assertEquals(tenant.getTitle(), tenantDto.getTitle());
    Assertions.assertEquals(tenant.getDescription(), tenantDto.getDescription());
  }
}