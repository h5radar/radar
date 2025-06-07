package com.h5radar.radar.domain.radar_user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.domain.AbstractIntegrationTests;


class RadarUserIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private RadarUserService radarUserService;

  @Test
  @WithMockUser
  public void shouldGetTechnologies() {
    // Create technology
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setTitle("My title");
    technologyDto.setDescription("My description");
    technologyDto.setWebsite("My website");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);
    technologyDto = radarUserService.save(technologyDto);

    webTestClient.get().uri("/api/v1/radar-users")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(technologyDto.getId())
        .jsonPath("$.content[0].title").isEqualTo(technologyDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(technologyDto.getDescription())
        .jsonPath("$.content[0].website").isEqualTo(technologyDto.getWebsite())
        .jsonPath("$.content[0].moved").isEqualTo(technologyDto.getMoved())
        .jsonPath("$.content[0].active").isEqualTo(technologyDto.isActive());

    radarUserService.deleteById(technologyDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldGetRadarUser() {
    // Create technology
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setTitle("My title");
    technologyDto.setDescription("My description");
    technologyDto.setWebsite("My website");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);
    technologyDto = radarUserService.save(technologyDto);

    webTestClient.get().uri("/api/v1/radar-users/{id}", technologyDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(technologyDto.getId())
        .jsonPath("$.title").isEqualTo(technologyDto.getTitle())
        .jsonPath("$.description").isEqualTo(technologyDto.getDescription())
        .jsonPath("$.website").isEqualTo(technologyDto.getWebsite())
        .jsonPath("$.moved").isEqualTo(technologyDto.getMoved())
        .jsonPath("$.active").isEqualTo(technologyDto.isActive());

    radarUserService.deleteById(technologyDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateRadarUser() throws Exception {
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    RadarUserDto technologyDto1 = webTestClient.post().uri("/api/v1/radar-users")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), RadarUserDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(RadarUserDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(technologyDto.getId(), technologyDto1.getId());
    Assertions.assertEquals(technologyDto.getTitle(), technologyDto1.getTitle());
    Assertions.assertEquals(technologyDto.getDescription(), technologyDto1.getDescription());
    Assertions.assertEquals(technologyDto.getWebsite(), technologyDto1.getWebsite());
    Assertions.assertEquals(technologyDto.getMoved(), technologyDto1.getMoved());
    Assertions.assertEquals(technologyDto.isActive(), technologyDto1.isActive());

    radarUserService.deleteById(technologyDto1.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateRadarUserWithId() throws Exception {
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(99L);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    RadarUserDto technologyDto1 = webTestClient.post().uri("/api/v1/radar-users")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), RadarUserDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(RadarUserDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(technologyDto.getId(), technologyDto1.getId());
    Assertions.assertEquals(technologyDto.getTitle(), technologyDto1.getTitle());
    Assertions.assertEquals(technologyDto.getDescription(), technologyDto1.getDescription());
    Assertions.assertEquals(technologyDto.getWebsite(), technologyDto1.getWebsite());
    Assertions.assertEquals(technologyDto.getMoved(), technologyDto1.getMoved());
    Assertions.assertEquals(technologyDto.isActive(), technologyDto1.isActive());

    radarUserService.deleteById(technologyDto1.getId());
  }

  @Test
  @WithMockUser
  public void shouldUpdateRadarUser() throws Exception {
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);
    technologyDto = radarUserService.save(technologyDto);

    webTestClient.put().uri("/api/v1/radar-users/{id}", technologyDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), RadarUserDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(technologyDto.getId());
  }


  @Test
  @WithMockUser
  public void shouldDeleteRadarUser() throws Exception {
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);
    technologyDto = radarUserService.save(technologyDto);

    webTestClient.delete().uri("/api/v1/radar-users/{id}", technologyDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();
  }
}
