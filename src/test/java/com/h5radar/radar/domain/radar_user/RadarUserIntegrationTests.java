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
    // Create technology
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");
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
        .jsonPath("$.content[0].sub").isEqualTo(technologyDto.getSub())
        .jsonPath("$.content[0].username").isEqualTo(technologyDto.getUsername());

    radarUserService.deleteById(technologyDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldGetRadarUser() {
    // Create technology
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");
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
        .jsonPath("$.sub").isEqualTo(technologyDto.getSub())
        .jsonPath("$.username").isEqualTo(technologyDto.getUsername());

    radarUserService.deleteById(technologyDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateRadarUser() throws Exception {
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");

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
    Assertions.assertEquals(technologyDto.getSub(), technologyDto1.getSub());
    Assertions.assertEquals(technologyDto.getUsername(), technologyDto1.getUsername());

    radarUserService.deleteById(technologyDto1.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateRadarUserWithId() throws Exception {
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(99L);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");

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
    Assertions.assertEquals(technologyDto.getSub(), technologyDto1.getSub());
    Assertions.assertEquals(technologyDto.getUsername(), technologyDto1.getUsername());

    radarUserService.deleteById(technologyDto1.getId());
  }

  @Test
  @WithMockUser
  public void shouldUpdateRadarUser() throws Exception {
    RadarUserDto technologyDto = new RadarUserDto();
    technologyDto.setId(null);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");
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
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");
    technologyDto = radarUserService.save(technologyDto);

    webTestClient.delete().uri("/api/v1/radar-users/{id}", technologyDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();
  }
}
