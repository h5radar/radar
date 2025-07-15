package com.h5radar.radar.radar_user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.h5radar.radar.AbstractIntegrationTests;


class RadarUserIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private RadarUserService radarUserService;

  @Test
  @WithMockUser(value = "My sub")
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
  @WithMockUser(value = "My sub")
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
}
