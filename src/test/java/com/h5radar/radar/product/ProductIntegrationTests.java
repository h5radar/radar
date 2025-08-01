package com.h5radar.radar.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.radar.AbstractIntegrationTests;
import com.h5radar.radar.radar_user.RadarUserDto;
import com.h5radar.radar.radar_user.RadarUserService;


class ProductIntegrationTests extends AbstractIntegrationTests {
  @Autowired
  private ProductService productService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetProducts() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create product
    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setRadarUserId(radarUserDto.getId());
    productDto.setTitle("My title");
    productDto.setDescription("My description");
    productDto = productService.save(productDto);

    webTestClient.get().uri("/api/v1/products")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(productDto.getId())
        .jsonPath("$.content[0].radar_user_id").isEqualTo(productDto.getRadarUserId())
        .jsonPath("$.content[0].title").isEqualTo(productDto.getTitle())
        .jsonPath("$.content[0].description").isEqualTo(productDto.getDescription());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetProduct() {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    // Create product
    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setRadarUserId(radarUserDto.getId());
    productDto.setTitle("My title");
    productDto.setDescription("My description");
    productDto = productService.save(productDto);

    webTestClient.get().uri("/api/v1/products/{id}", productDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(productDto.getId())
        .jsonPath("$.radar_user_id").isEqualTo(productDto.getRadarUserId())
        .jsonPath("$.title").isEqualTo(productDto.getTitle())
        .jsonPath("$.description").isEqualTo(productDto.getDescription());

    radarUserService.deleteById(radarUserDto.getId());
    productService.deleteById(productDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateProduct() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setRadarUserId(radarUserDto.getId());
    productDto.setTitle("My product");
    productDto.setDescription("My product description");

    ProductDto productDto1 = webTestClient.post().uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(productDto), ProductDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(ProductDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(productDto.getId(), productDto1.getId());
    Assertions.assertEquals(productDto.getRadarUserId(), productDto1.getRadarUserId());
    Assertions.assertEquals(productDto.getTitle(), productDto1.getTitle());
    Assertions.assertEquals(productDto.getDescription(), productDto1.getDescription());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateProductWithId() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    ProductDto productDto = new ProductDto();
    productDto.setId(99L);
    productDto.setRadarUserId(radarUserDto.getId());
    productDto.setTitle("My product");
    productDto.setDescription("My product description");

    ProductDto productDto1 = webTestClient.post().uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(productDto), ProductDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(ProductDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(productDto.getId(), productDto1.getId());
    Assertions.assertEquals(productDto.getRadarUserId(), productDto1.getRadarUserId());
    Assertions.assertEquals(productDto.getTitle(), productDto1.getTitle());
    Assertions.assertEquals(productDto.getDescription(), productDto1.getDescription());

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateProductWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setRadarUserId(null);
    productDto.setTitle("My product");
    productDto.setDescription("My product description");

    ProductDto productDto1 = webTestClient.post().uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(productDto), ProductDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(ProductDto.class)
        .returnResult()
        .getResponseBody();

    Assertions.assertNotEquals(productDto.getId(), productDto1.getId());
    Assertions.assertEquals(radarUserDto.getId(), productDto1.getRadarUserId());
    Assertions.assertEquals(productDto.getTitle(), productDto1.getTitle());
    Assertions.assertEquals(productDto.getDescription(), productDto1.getDescription());

    radarUserService.deleteById(radarUserDto.getId());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateProduct() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setRadarUserId(radarUserDto.getId());
    productDto.setTitle("My product");
    productDto.setDescription("My product description");
    productDto = productService.save(productDto);

    webTestClient.put().uri("/api/v1/products/{id}", productDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(productDto), ProductDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateProductWithoutUser() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setRadarUserId(radarUserDto.getId());
    productDto.setTitle("My product");
    productDto.setDescription("My product description");
    productDto = productService.save(productDto);

    productDto.setRadarUserId(null);
    webTestClient.put().uri("/api/v1/products/{id}", productDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(productDto), ProductDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteProduct() throws Exception {
    // Create radar user
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setRadarUserId(radarUserDto.getId());
    productDto.setTitle("My product");
    productDto.setDescription("My product description");
    productDto = productService.save(productDto);

    webTestClient.delete().uri("/api/v1/products/{id}", productDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    radarUserService.deleteById(radarUserDto.getId());
  }
}
