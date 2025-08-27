package com.h5radar.radar.license;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.AbstractIntegrationTests;
import com.h5radar.radar.compliance.ComplianceDto;
import com.h5radar.radar.compliance.ComplianceService;
import com.h5radar.radar.radar_user.RadarUserDto;


class LicenseIntegrationTests extends AbstractIntegrationTests {
  @Autowired
  protected ComplianceService complianceService;

  @Autowired
  private LicenseService licenseService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetLicenses() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My title");
    licenseDto.setDescription("My description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);
    licenseDto = licenseService.save(licenseDto);

    webTestClient.get().uri("/api/v1/licenses")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(licenseDto.getId())
        .jsonPath("$.content[0].radar_user_id").isEqualTo(licenseDto.getRadarUserId())
        .jsonPath("$.content[0].title").isEqualTo(licenseDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(licenseDto.getDescription())
        .jsonPath("$.content[0].compliance_id").isEqualTo(licenseDto.getComplianceId())
        .jsonPath("$.content[0].active").isEqualTo(licenseDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetLicense() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My title");
    licenseDto.setDescription("My description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);
    licenseDto = licenseService.save(licenseDto);

    webTestClient.get().uri("/api/v1/licenses/{id}", licenseDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(licenseDto.getId())
        .jsonPath("$.radar_user_id").isEqualTo(licenseDto.getRadarUserId())
        .jsonPath("$.title").isEqualTo(licenseDto.getTitle())
        .jsonPath("$.description").isEqualTo(licenseDto.getDescription())
        .jsonPath("$.compliance_id").isEqualTo(licenseDto.getComplianceId())
        .jsonPath("$.active").isEqualTo(licenseDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateLicense() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);

    LicenseDto licenseDto1 = webTestClient.post().uri("/api/v1/licenses")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(licenseDto), LicenseDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(LicenseDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(licenseDto.getId(), licenseDto1.getId());
    Assertions.assertEquals(licenseDto.getRadarUserId(), licenseDto1.getRadarUserId());
    Assertions.assertEquals(licenseDto.getTitle(), licenseDto1.getTitle());
    Assertions.assertEquals(licenseDto.getDescription(), licenseDto1.getDescription());
    Assertions.assertEquals(licenseDto.getComplianceId(), licenseDto1.getComplianceId());
    Assertions.assertEquals(licenseDto.isActive(), licenseDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateLicenseWithId() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(99L);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);

    LicenseDto licenseDto1 = webTestClient.post().uri("/api/v1/licenses")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(licenseDto), LicenseDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(LicenseDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(licenseDto.getId(), licenseDto1.getId());
    Assertions.assertEquals(licenseDto.getRadarUserId(), licenseDto1.getRadarUserId());
    Assertions.assertEquals(licenseDto.getTitle(), licenseDto1.getTitle());
    Assertions.assertEquals(licenseDto.getDescription(), licenseDto1.getDescription());
    Assertions.assertEquals(licenseDto.getComplianceId(), licenseDto1.getComplianceId());
    Assertions.assertEquals(licenseDto.isActive(), licenseDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateLicenseWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserId(null);
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);

    LicenseDto licenseDto1 = webTestClient.post().uri("/api/v1/licenses")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(licenseDto), LicenseDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(LicenseDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(licenseDto.getId(), licenseDto1.getId());
    Assertions.assertEquals(radarUserDto.getId(), licenseDto1.getRadarUserId());
    Assertions.assertEquals(licenseDto.getTitle(), licenseDto1.getTitle());
    Assertions.assertEquals(licenseDto.getDescription(), licenseDto1.getDescription());
    Assertions.assertEquals(licenseDto.getComplianceId(), licenseDto1.getComplianceId());
    Assertions.assertEquals(licenseDto.isActive(), licenseDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateLicense() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);
    licenseDto = licenseService.save(licenseDto);

    webTestClient.put().uri("/api/v1/licenses/{id}", licenseDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(licenseDto), LicenseDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateLicenseWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);
    licenseDto = licenseService.save(licenseDto);


    licenseDto.setRadarUserId(null);
    webTestClient.put().uri("/api/v1/licenses/{id}", licenseDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(licenseDto), LicenseDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteLicense() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceId(complianceDto.getId());
    licenseDto.setActive(true);
    licenseDto = licenseService.save(licenseDto);

    webTestClient.delete().uri("/api/v1/licenses/{id}", licenseDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedLicenses() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance to seed licences
    for (String compliance : Arrays.asList("High1", "Medium1", "Low2")) {
      ComplianceDto complianceDto = new ComplianceDto();
      complianceDto.setId(null);
      complianceDto.setRadarUserId(radarUserDto.getId());
      complianceDto.setTitle(compliance);
      complianceDto.setDescription("My description");
      complianceDto.setActive(true);
      complianceService.save(complianceDto);
    }

    webTestClient.post().uri("/api/v1/licenses/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();

    radarUserService.deleteById(radarUserDto.getId());
  }
}
