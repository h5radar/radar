package com.h5radar.radar.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.AbstractIntegrationTests;
import com.h5radar.radar.radar_user.RadarUserDto;


class DomainIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private DomainService domainService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetDomains() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(null);
    domainDto.setRadarUserId(radarUserDto.getId());
    domainDto.setTitle("My title");
    domainDto.setDescription("My description");
    domainDto.setPosition(0);
    domainDto = domainService.save(domainDto);

    webTestClient.get().uri("/api/v1/domains")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(domainDto.getId())
        .jsonPath("$.content[0].radar_user_id").isEqualTo(domainDto.getRadarUserId())
        .jsonPath("$.content[0].title").isEqualTo(domainDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(domainDto.getDescription())
        .jsonPath("$.content[0].position").isEqualTo(domainDto.getPosition());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetDomain() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(null);
    domainDto.setRadarUserId(radarUserDto.getId());
    domainDto.setTitle("My title");
    domainDto.setDescription("My description");
    domainDto.setPosition(0);
    domainDto = domainService.save(domainDto);

    webTestClient.get().uri("/api/v1/domains/{id}", domainDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(domainDto.getId())
        .jsonPath("$.radar_user_id").isEqualTo(domainDto.getRadarUserId())
        .jsonPath("$.title").isEqualTo(domainDto.getTitle())
        .jsonPath("$.description").isEqualTo(domainDto.getDescription())
        .jsonPath("$.position").isEqualTo(domainDto.getPosition());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateDomain() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(null);
    domainDto.setRadarUserId(radarUserDto.getId());
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);

    DomainDto domainDto1 = webTestClient.post().uri("/api/v1/domains")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(domainDto), DomainDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(DomainDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(domainDto.getId(), domainDto1.getId());
    Assertions.assertEquals(domainDto.getRadarUserId(), domainDto1.getRadarUserId());
    Assertions.assertEquals(domainDto.getTitle(), domainDto1.getTitle());
    Assertions.assertEquals(domainDto.getDescription(), domainDto1.getDescription());
    Assertions.assertEquals(domainDto.getPosition(), domainDto1.getPosition());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateDomainWithId() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(99L);
    domainDto.setRadarUserId(radarUserDto.getId());
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);

    DomainDto domainDto1 = webTestClient.post().uri("/api/v1/domains")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(domainDto), DomainDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(DomainDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(domainDto.getId(), domainDto1.getId());
    Assertions.assertEquals(domainDto.getRadarUserId(), domainDto1.getRadarUserId());
    Assertions.assertEquals(domainDto.getTitle(), domainDto1.getTitle());
    Assertions.assertEquals(domainDto.getDescription(), domainDto1.getDescription());
    Assertions.assertEquals(domainDto.getPosition(), domainDto1.getPosition());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateDomainWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(null);
    domainDto.setRadarUserId(null);
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);

    DomainDto domainDto1 = webTestClient.post().uri("/api/v1/domains")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(domainDto), DomainDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(DomainDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(domainDto.getId(), domainDto1.getId());
    Assertions.assertEquals(radarUserDto.getId(), domainDto1.getRadarUserId());
    Assertions.assertEquals(domainDto.getTitle(), domainDto1.getTitle());
    Assertions.assertEquals(domainDto.getDescription(), domainDto1.getDescription());
    Assertions.assertEquals(domainDto.getPosition(), domainDto1.getPosition());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateDomain() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(null);
    domainDto.setRadarUserId(radarUserDto.getId());
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);
    domainDto = domainService.save(domainDto);

    webTestClient.put().uri("/api/v1/domains/{id}", domainDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(domainDto), DomainDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateDomainWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(null);
    domainDto.setRadarUserId(radarUserDto.getId());
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);
    domainDto = domainService.save(domainDto);

    domainDto.setRadarUserId(null);
    webTestClient.put().uri("/api/v1/domains/{id}", domainDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(domainDto), DomainDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteDomain() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create domain
    DomainDto domainDto = new DomainDto();
    domainDto.setId(null);
    domainDto.setRadarUserId(radarUserDto.getId());
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);
    domainDto = domainService.save(domainDto);

    webTestClient.delete().uri("/api/v1/domains/{id}", domainDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedDomains() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.post().uri("/api/v1/domains/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();

    radarUserService.deleteById(radarUserDto.getId());
  }
}
