package com.h5radar.radar.domain.compliance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.domain.AbstractIntegrationTests;
import com.h5radar.radar.domain.radar_user.RadarUserDto;


class ComplianceIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private ComplianceService complianceService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetCompliances() {
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

    webTestClient.get().uri("/api/v1/compliances")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(complianceDto.getId())
        .jsonPath("$.content[0].radar_user_id").isEqualTo(complianceDto.getRadarUserId())
        .jsonPath("$.content[0].title").isEqualTo(complianceDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(complianceDto.getDescription())
        .jsonPath("$.content[0].active").isEqualTo(complianceDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetCompliance() {
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

    webTestClient.get().uri("/api/v1/compliances/{id}", complianceDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(complianceDto.getId())
        .jsonPath("$.radar_user_id").isEqualTo(complianceDto.getRadarUserId())
        .jsonPath("$.title").isEqualTo(complianceDto.getTitle())
        .jsonPath("$.description").isEqualTo(complianceDto.getDescription())
        .jsonPath("$.active").isEqualTo(complianceDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateCompliance() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);

    ComplianceDto complianceDto1 = webTestClient.post().uri("/api/v1/compliances")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(complianceDto), ComplianceDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(ComplianceDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(complianceDto.getId(), complianceDto1.getId());
    Assertions.assertEquals(complianceDto.getRadarUserId(), complianceDto1.getRadarUserId());
    Assertions.assertEquals(complianceDto.getTitle(), complianceDto1.getTitle());
    Assertions.assertEquals(complianceDto.getDescription(), complianceDto1.getDescription());
    Assertions.assertEquals(complianceDto.isActive(), complianceDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateComplianceWithId() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(99L);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);

    ComplianceDto complianceDto1 = webTestClient.post().uri("/api/v1/compliances")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(complianceDto), ComplianceDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(ComplianceDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(complianceDto.getId(), complianceDto1.getId());
    Assertions.assertEquals(complianceDto.getRadarUserId(), complianceDto1.getRadarUserId());
    Assertions.assertEquals(complianceDto.getTitle(), complianceDto1.getTitle());
    Assertions.assertEquals(complianceDto.getDescription(), complianceDto1.getDescription());
    Assertions.assertEquals(complianceDto.isActive(), complianceDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateComplianceWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(null);
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);

    ComplianceDto complianceDto1 = webTestClient.post().uri("/api/v1/compliances")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(complianceDto), ComplianceDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(ComplianceDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(complianceDto.getId(), complianceDto1.getId());
    Assertions.assertEquals(radarUserDto.getId(), complianceDto1.getRadarUserId());
    Assertions.assertEquals(complianceDto.getTitle(), complianceDto1.getTitle());
    Assertions.assertEquals(complianceDto.getDescription(), complianceDto1.getDescription());
    Assertions.assertEquals(complianceDto.isActive(), complianceDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateCompliance() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    webTestClient.put().uri("/api/v1/compliances/{id}", complianceDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(complianceDto), ComplianceDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateComplianceWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    complianceDto.setRadarUserId(null);
    webTestClient.put().uri("/api/v1/compliances/{id}", complianceDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(complianceDto), ComplianceDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteCompliance() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create compliance
    ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);
    complianceDto = complianceService.save(complianceDto);

    webTestClient.delete().uri("/api/v1/compliances/{id}", complianceDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedCompliances() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.post().uri("/api/v1/compliances/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();

    radarUserService.deleteById(radarUserDto.getId());
  }
}
