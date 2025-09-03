package com.h5radar.radar.practice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.AbstractIntegrationTests;
import com.h5radar.radar.radar_user.RadarUserDto;
import com.h5radar.radar.radar_user.RadarUserService;


class PracticeIntegrationTests extends AbstractIntegrationTests {
  @Autowired
  private PracticeService practiceService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetPractices() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My title");
    practiceDto.setDescription("My description");
    practiceDto.setActive(true);
    practiceDto = practiceService.save(practiceDto);

    webTestClient.get().uri("/api/v1/practices")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(practiceDto.getId())
        .jsonPath("$.content[0].radar_user.id").isEqualTo(practiceDto.getRadarUserDto().getId())
        .jsonPath("$.content[0].radar_user.sub").isEqualTo(practiceDto.getRadarUserDto().getSub())
        .jsonPath("$.content[0].title").isEqualTo(practiceDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(practiceDto.getDescription())
        .jsonPath("$.content[0].active").isEqualTo(practiceDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetPractice() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My title");
    practiceDto.setDescription("My description");
    practiceDto.setActive(true);
    practiceDto = practiceService.save(practiceDto);

    webTestClient.get().uri("/api/v1/practices/{id}", practiceDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(practiceDto.getId())
        .jsonPath("$.radar_user.id").isEqualTo(practiceDto.getRadarUserDto().getId())
        .jsonPath("$.radar_user.sub").isEqualTo(practiceDto.getRadarUserDto().getSub())
        .jsonPath("$.title").isEqualTo(practiceDto.getTitle())
        .jsonPath("$.description").isEqualTo(practiceDto.getDescription())
        .jsonPath("$.active").isEqualTo(practiceDto.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreatePractice() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);

    PracticeDto practiceDto1 = webTestClient.post().uri("/api/v1/practices")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(practiceDto), PracticeDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(PracticeDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(practiceDto.getId(), practiceDto1.getId());
    Assertions.assertEquals(practiceDto.getRadarUserDto().getId(), practiceDto1.getRadarUserDto().getId());
    Assertions.assertEquals(practiceDto.getRadarUserDto().getSub(), practiceDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(practiceDto.getTitle(), practiceDto1.getTitle());
    Assertions.assertEquals(practiceDto.getDescription(), practiceDto1.getDescription());
    Assertions.assertEquals(practiceDto.isActive(), practiceDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreatePracticeWithId() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(99L);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);

    PracticeDto practiceDto1 = webTestClient.post().uri("/api/v1/practices")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(practiceDto), PracticeDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(PracticeDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(practiceDto.getId(), practiceDto1.getId());
    Assertions.assertEquals(practiceDto.getRadarUserDto().getId(), practiceDto1.getRadarUserDto().getId());
    Assertions.assertEquals(practiceDto.getRadarUserDto().getSub(), practiceDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(practiceDto.getTitle(), practiceDto1.getTitle());
    Assertions.assertEquals(practiceDto.getDescription(), practiceDto1.getDescription());
    Assertions.assertEquals(practiceDto.isActive(), practiceDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreatePracticeWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(null);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);

    PracticeDto practiceDto1 = webTestClient.post().uri("/api/v1/practices")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(practiceDto), PracticeDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(PracticeDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(practiceDto.getId(), practiceDto1.getId());
    Assertions.assertEquals(radarUserDto.getId(), practiceDto1.getRadarUserDto().getId());
    Assertions.assertEquals(radarUserDto.getSub(), practiceDto1.getRadarUserDto().getSub());
    Assertions.assertEquals(practiceDto.getTitle(), practiceDto1.getTitle());
    Assertions.assertEquals(practiceDto.getDescription(), practiceDto1.getDescription());
    Assertions.assertEquals(practiceDto.isActive(), practiceDto1.isActive());

    radarUserService.deleteById(radarUserDto.getId());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdatePractice() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);
    practiceDto = practiceService.save(practiceDto);

    webTestClient.put().uri("/api/v1/practices/{id}", practiceDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(practiceDto), PracticeDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdatePracticeWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);
    practiceDto = practiceService.save(practiceDto);

    practiceDto.setRadarUserDto(null);
    webTestClient.put().uri("/api/v1/practices/{id}", practiceDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(practiceDto), PracticeDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeletePractice() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create practice
    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);
    practiceDto = practiceService.save(practiceDto);

    webTestClient.delete().uri("/api/v1/practices/{id}", practiceDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedPractices() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.post().uri("/api/v1/practices/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();

    radarUserService.deleteById(radarUserDto.getId());
  }
}
