package com.t9radar.radar.domain.tenant;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.t9radar.radar.domain.AbstractIntegrationTests;


/* TODO: uncomment
class TenantIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private TenantService tenantService;

  @Test
  public void shouldGetTenants() {
    // Create tenant
    TenantDto tenantDto = new TenantDto();
    tenantDto.setTitle("My title");
    tenantDto.setDescription("My description");
    tenantDto = tenantService.save(tenantDto);

    ResponseEntity<List<Tenant>> responseEntity =
        restTemplate.exchange(baseUrl + port + "/api/v1/tenants", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Tenant>>() {
            });
    List<Tenant> tenantList = responseEntity.getBody();
    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertEquals(1, tenantList.size());

    this.tenantService.deleteById(tenantDto.getId());
  }
}
*/