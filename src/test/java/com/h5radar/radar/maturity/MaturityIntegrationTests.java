package com.h5radar.radar.maturity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.AbstractIntegrationTests;
import com.h5radar.radar.radar_user.RadarUserDto;


class MaturityIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private MaturityService maturityService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetMaturities() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");
    maturityDto = maturityService.save(maturityDto);

    webTestClient.get().uri("/api/v1/maturities")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(maturityDto.getId())
        .jsonPath("$.content[0].radar_user.id").isEqualTo(maturityDto.getRadarUserDto().getId())
        .jsonPath("$.content[0].radar_user.sub").isEqualTo(maturityDto.getRadarUserDto().getSub())
        .jsonPath("$.content[0].title").isEqualTo(maturityDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(maturityDto.getDescription())
        .jsonPath("$.content[0].position").isEqualTo(maturityDto.getPosition())
        .jsonPath("$.content[0].color").isEqualTo(maturityDto.getColor());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetMaturity() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");
    maturityDto = maturityService.save(maturityDto);

    webTestClient.get().uri("/api/v1/maturities/{id}", maturityDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(maturityDto.getId())
        .jsonPath("$.radar_user.id").isEqualTo(maturityDto.getRadarUserDto().getId())
        .jsonPath("$.radar_user.sub").isEqualTo(maturityDto.getRadarUserDto().getSub())
        .jsonPath("$.title").isEqualTo(maturityDto.getTitle())
        .jsonPath("$.description").isEqualTo(maturityDto.getDescription())
        .jsonPath("$.position").isEqualTo(maturityDto.getPosition())
        .jsonPath("$.color").isEqualTo(maturityDto.getColor());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateMaturity() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");

    MaturityDto maturityDto1 = webTestClient.post().uri("/api/v1/maturities")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(maturityDto), MaturityDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(MaturityDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(maturityDto.getId(), maturityDto1.getId());
    Assertions.assertEquals(maturityDto.getRadarUserDto().getId(), maturityDto1.getRadarUserDto().getId());
    Assertions.assertEquals(maturityDto.getRadarUserDto().getSub(), maturityDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(maturityDto.getTitle(), maturityDto1.getTitle());
    Assertions.assertEquals(maturityDto.getDescription(), maturityDto1.getDescription());
    Assertions.assertEquals(maturityDto.getPosition(), maturityDto1.getPosition());
    Assertions.assertEquals(maturityDto.getColor(), maturityDto1.getColor());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateMaturityWithId() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(99L);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");

    MaturityDto maturityDto1 = webTestClient.post().uri("/api/v1/maturities")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(maturityDto), MaturityDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(MaturityDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(maturityDto.getId(), maturityDto1.getId());
    Assertions.assertEquals(maturityDto.getRadarUserDto().getId(), maturityDto1.getRadarUserDto().getId());
    Assertions.assertEquals(maturityDto.getRadarUserDto().getSub(), maturityDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(maturityDto.getTitle(), maturityDto1.getTitle());
    Assertions.assertEquals(maturityDto.getDescription(), maturityDto1.getDescription());
    Assertions.assertEquals(maturityDto.getPosition(), maturityDto1.getPosition());
    Assertions.assertEquals(maturityDto.getColor(), maturityDto1.getColor());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateMaturityWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(null);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");

    MaturityDto maturityDto1 = webTestClient.post().uri("/api/v1/maturities")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(maturityDto), MaturityDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(MaturityDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(maturityDto.getId(), maturityDto1.getId());
    Assertions.assertEquals(radarUserDto.getId(), maturityDto1.getRadarUserDto().getId());
    Assertions.assertEquals(radarUserDto.getSub(), maturityDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(maturityDto.getTitle(), maturityDto1.getTitle());
    Assertions.assertEquals(maturityDto.getDescription(), maturityDto1.getDescription());
    Assertions.assertEquals(maturityDto.getPosition(), maturityDto1.getPosition());
    Assertions.assertEquals(maturityDto.getColor(), maturityDto1.getColor());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateMaturity() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");
    maturityDto = maturityService.save(maturityDto);

    webTestClient.put().uri("/api/v1/maturities/{id}", maturityDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(maturityDto), MaturityDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateMaturityWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");
    maturityDto = maturityService.save(maturityDto);

    maturityDto.setRadarUserDto(null);
    webTestClient.put().uri("/api/v1/maturities/{id}", maturityDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(maturityDto), MaturityDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteMaturity() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create maturity
    MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("ADOPT");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");
    maturityDto = maturityService.save(maturityDto);

    webTestClient.delete().uri("/api/v1/maturities/{id}", maturityDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedMaturities() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.post().uri("/api/v1/maturities/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();

    radarUserService.deleteById(radarUserDto.getId());
  }
}
