package com.h5radar.radar.domain.technology;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.domain.AbstractIntegrationTests;


class TechnologyIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private TechnologyService technologyService;

  @Test
  @WithMockUser
  public void shouldGetTechnologies() {
    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setTitle("My title");
    technologyDto.setDescription("My description");
    technologyDto.setWebsite("My website");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);
    technologyDto = technologyService.save(technologyDto);

    webTestClient.get().uri("/api/v1/technologies")
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

    technologyService.deleteById(technologyDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldGetTechnology() {
    // Create technology
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
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
        .jsonPath("$.title").isEqualTo(technologyDto.getTitle())
        .jsonPath("$.description").isEqualTo(technologyDto.getDescription())
        .jsonPath("$.website").isEqualTo(technologyDto.getWebsite())
        .jsonPath("$.moved").isEqualTo(technologyDto.getMoved())
        .jsonPath("$.active").isEqualTo(technologyDto.isActive());

    technologyService.deleteById(technologyDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateTechnology() throws Exception {
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);
    technologyDto = technologyService.save(technologyDto);

    webTestClient.post().uri("/api/v1/technologies")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(technologyDto), TechnologyDto.class)
        .exchange()
        .expectStatus().isCreated()
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
    technologyService.deleteById(technologyDto.getId());
  }


  @Test
  @WithMockUser
  public void shouldUpdateTechnology() throws Exception {
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
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

    technologyService.deleteById(technologyDto.getId());
  }


  @Test
  @WithMockUser
  public void shouldDeleteTechnology() throws Exception {
    TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(null);
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
  }
}
