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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserDto(radarUserDto);
    licenseDto.setTitle("My title");
    licenseDto.setDescription("My description");
    licenseDto.setComplianceDto(complianceDto);
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
        .jsonPath("$.content[0].radar_user_id").isEqualTo(licenseDto.getRadarUserDto().getId())
        .jsonPath("$.content[0].title").isEqualTo(licenseDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(licenseDto.getDescription())
        .jsonPath("$.content[0].compliance.id").isEqualTo(licenseDto.getComplianceDto().getId())
        .jsonPath("$.content[0].compliance.title").isEqualTo(licenseDto.getComplianceDto().getTitle())
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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserDto(radarUserDto);
    licenseDto.setTitle("My title");
    licenseDto.setDescription("My description");
    licenseDto.setComplianceDto(complianceDto);
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
        .jsonPath("$.radar_user_id").isEqualTo(licenseDto.getRadarUserDto().getId())
        .jsonPath("$.title").isEqualTo(licenseDto.getTitle())
        .jsonPath("$.description").isEqualTo(licenseDto.getDescription())
        .jsonPath("$.compliance.id").isEqualTo(licenseDto.getComplianceDto().getId())
        .jsonPath("$.compliance.title").isEqualTo(licenseDto.getComplianceDto().getTitle())
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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserDto(radarUserDto);
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceDto(complianceDto);
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
    Assertions.assertEquals(licenseDto.getRadarUserDto().getId(), licenseDto1.getRadarUserDto().getId());
    Assertions.assertEquals(licenseDto.getRadarUserDto().getSub(), licenseDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(licenseDto.getTitle(), licenseDto1.getTitle());
    Assertions.assertEquals(licenseDto.getDescription(), licenseDto1.getDescription());
    Assertions.assertEquals(licenseDto.getComplianceDto().getId(), licenseDto1.getComplianceDto().getId());
    Assertions.assertEquals(licenseDto.getComplianceDto().getTitle(), licenseDto1.getComplianceDto().getTitle());
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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(99L);
    licenseDto.setRadarUserDto(radarUserDto);
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceDto(complianceDto);
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
    Assertions.assertEquals(licenseDto.getRadarUserDto().getId(), licenseDto1.getRadarUserDto().getId());
    Assertions.assertEquals(licenseDto.getRadarUserDto().getSub(), licenseDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(licenseDto.getTitle(), licenseDto1.getTitle());
    Assertions.assertEquals(licenseDto.getDescription(), licenseDto1.getDescription());
    Assertions.assertEquals(licenseDto.getComplianceDto().getId(), licenseDto1.getComplianceDto().getId());
    Assertions.assertEquals(licenseDto.getComplianceDto().getTitle(), licenseDto1.getComplianceDto().getTitle());
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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserDto(null);
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceDto(complianceDto);
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
    Assertions.assertEquals(radarUserDto.getId(), licenseDto1.getRadarUserDto().getId());
    Assertions.assertEquals(radarUserDto.getSub(), licenseDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(licenseDto.getTitle(), licenseDto1.getTitle());
    Assertions.assertEquals(licenseDto.getDescription(), licenseDto1.getDescription());
    Assertions.assertEquals(licenseDto.getComplianceDto().getId(), licenseDto1.getComplianceDto().getId());
    Assertions.assertEquals(licenseDto.getComplianceDto().getTitle(), licenseDto1.getComplianceDto().getTitle());
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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserDto(radarUserDto);
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceDto(complianceDto);
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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserDto(radarUserDto);
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceDto(complianceDto);
    licenseDto.setActive(true);
    licenseDto = licenseService.save(licenseDto);


    licenseDto.setRadarUserDto(null);
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
    complianceDto.setRadarUserDto(radarUserDto);
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    // Create license
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(null);
    licenseDto.setRadarUserDto(radarUserDto);
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setComplianceDto(complianceDto);
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
    for (String compliance : Arrays.asList("High", "Medium", "Low")) {
      ComplianceDto complianceDto = new ComplianceDto();
      complianceDto.setId(null);
      complianceDto.setRadarUserDto(radarUserDto);
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
