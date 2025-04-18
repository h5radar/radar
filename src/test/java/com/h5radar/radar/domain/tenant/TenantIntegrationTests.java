package com.h5radar.radar.domain.tenant;


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