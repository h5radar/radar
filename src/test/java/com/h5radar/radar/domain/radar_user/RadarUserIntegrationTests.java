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
  public void shouldGetRadarUsers() {
    // Create radarUser
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.get().uri("/api/v1/radar-users")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(radarUserDto.getId())
        .jsonPath("$.content[0].sub").isEqualTo(radarUserDto.getSub())
        .jsonPath("$.content[0].username").isEqualTo(radarUserDto.getUsername());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldGetRadarUser() {
    // Create radarUser
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.get().uri("/api/v1/radar-users/{id}", radarUserDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(radarUserDto.getId())
        .jsonPath("$.sub").isEqualTo(radarUserDto.getSub())
        .jsonPath("$.username").isEqualTo(radarUserDto.getUsername());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateRadarUser() throws Exception {
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    RadarUserDto radarUserDto1 = webTestClient.post().uri("/api/v1/radar-users")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(radarUserDto), RadarUserDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(RadarUserDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(radarUserDto.getId(), radarUserDto1.getId());
    Assertions.assertEquals(radarUserDto.getSub(), radarUserDto1.getSub());
    Assertions.assertEquals(radarUserDto.getUsername(), radarUserDto1.getUsername());

    radarUserService.deleteById(radarUserDto1.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateRadarUserWithId() throws Exception {
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(99L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    RadarUserDto radarUserDto1 = webTestClient.post().uri("/api/v1/radar-users")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(radarUserDto), RadarUserDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(RadarUserDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(radarUserDto.getId(), radarUserDto1.getId());
    Assertions.assertEquals(radarUserDto.getSub(), radarUserDto1.getSub());
    Assertions.assertEquals(radarUserDto.getUsername(), radarUserDto1.getUsername());

    radarUserService.deleteById(radarUserDto1.getId());
  }

  @Test
  @WithMockUser
  public void shouldUpdateRadarUser() throws Exception {
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.put().uri("/api/v1/radar-users/{id}", radarUserDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(radarUserDto), RadarUserDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }


  @Test
  @WithMockUser
  public void shouldDeleteRadarUser() throws Exception {
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.delete().uri("/api/v1/radar-users/{id}", radarUserDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();
  }
}
