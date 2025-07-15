package com.h5radar.radar.technology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.AbstractIntegrationTests;
import com.h5radar.radar.radar_user.RadarUserDto;
import com.h5radar.radar.radar_user.RadarUserService;


class TechnologyIntegrationTests extends AbstractIntegrationTests {
  @Autowired
  private RadarUserService radarUserService;

  @Autowired
  private TechnologyService technologyService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetTechnologies() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setRadarUserId(radarUserDto.getId());
    technologyDto.setTitle("My title");
    technologyDto.setDescription("My description");
    technologyDto.setWebsite("My website");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);
    technologyDto = technologyService.save(technologyDto);

    webTestClient.get().uri("/api/v1/technologies")
        // .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(technologyDto.getId())
        .jsonPath("$.content[0].radar_user_id").isEqualTo(technologyDto.getRadarUserId())
        .jsonPath("$.content[0].title").isEqualTo(technologyDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(technologyDto.getDescription())
        .jsonPath("$.content[0].website").isEqualTo(technologyDto.getWebsite())
        .jsonPath("$.content[0].moved").isEqualTo(technologyDto.getMoved())
        .jsonPath("$.content[0].active").isEqualTo(technologyDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetTechnology() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setRadarUserId(radarUserDto.getId());
    technologyDto.setTitle("My title");
    technologyDto.setDescription("My description");
    technologyDto.setWebsite("My website");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);
    technologyDto = technologyService.save(technologyDto);

    webTestClient.get().uri("/api/v1/technologies/{id}", technologyDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(technologyDto.getId())
        .jsonPath("$.radar_user_id").isEqualTo(technologyDto.getRadarUserId())
        .jsonPath("$.title").isEqualTo(technologyDto.getTitle())
        .jsonPath("$.description").isEqualTo(technologyDto.getDescription())
        .jsonPath("$.website").isEqualTo(technologyDto.getWebsite())
        .jsonPath("$.moved").isEqualTo(technologyDto.getMoved())
        .jsonPath("$.active").isEqualTo(technologyDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateTechnology() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setRadarUserId(radarUserDto.getId());
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    TechnologyDto technologyDto1 = webTestClient.post().uri("/api/v1/technologies")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), TechnologyDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(TechnologyDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(technologyDto.getId(), technologyDto1.getId());
    Assertions.assertEquals(technologyDto.getRadarUserId(), technologyDto1.getRadarUserId());
    Assertions.assertEquals(technologyDto.getTitle(), technologyDto1.getTitle());
    Assertions.assertEquals(technologyDto.getDescription(), technologyDto1.getDescription());
    Assertions.assertEquals(technologyDto.getWebsite(), technologyDto1.getWebsite());
    Assertions.assertEquals(technologyDto.getMoved(), technologyDto1.getMoved());
    Assertions.assertEquals(technologyDto.isActive(), technologyDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateTechnologyWithId() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(99L);
    technologyDto.setRadarUserId(radarUserDto.getId());
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    TechnologyDto technologyDto1 = webTestClient.post().uri("/api/v1/technologies")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), TechnologyDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(TechnologyDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(technologyDto.getId(), technologyDto1.getId());
    Assertions.assertEquals(technologyDto.getRadarUserId(), technologyDto1.getRadarUserId());
    Assertions.assertEquals(technologyDto.getTitle(), technologyDto1.getTitle());
    Assertions.assertEquals(technologyDto.getDescription(), technologyDto1.getDescription());
    Assertions.assertEquals(technologyDto.getWebsite(), technologyDto1.getWebsite());
    Assertions.assertEquals(technologyDto.getMoved(), technologyDto1.getMoved());
    Assertions.assertEquals(technologyDto.isActive(), technologyDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateTechnologyWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setRadarUserId(null);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    TechnologyDto technologyDto1 = webTestClient.post().uri("/api/v1/technologies")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), TechnologyDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(TechnologyDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(technologyDto.getId(), technologyDto1.getId());
    Assertions.assertEquals(radarUserDto.getId(), technologyDto1.getRadarUserId());
    Assertions.assertEquals(technologyDto.getTitle(), technologyDto1.getTitle());
    Assertions.assertEquals(technologyDto.getDescription(), technologyDto1.getDescription());
    Assertions.assertEquals(technologyDto.getWebsite(), technologyDto1.getWebsite());
    Assertions.assertEquals(technologyDto.getMoved(), technologyDto1.getMoved());
    Assertions.assertEquals(technologyDto.isActive(), technologyDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateTechnology() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setRadarUserId(radarUserDto.getId());
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);
    technologyDto = technologyService.save(technologyDto);

    webTestClient.put().uri("/api/v1/technologies/{id}", technologyDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), TechnologyDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateTechnologyWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setRadarUserId(radarUserDto.getId());
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);
    technologyDto = technologyService.save(technologyDto);

    technologyDto.setRadarUserId(null);
    webTestClient.put().uri("/api/v1/technologies/{id}", technologyDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), TechnologyDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteTechnology() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setRadarUserId(radarUserDto.getId());
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);
    technologyDto = technologyService.save(technologyDto);

    webTestClient.delete().uri("/api/v1/technologies/{id}", technologyDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedTechnologies() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.post().uri("/api/v1/technologies/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();

    radarUserService.deleteById(radarUserDto.getId());
  }
}
