package com.t9radar.radar.domain.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.t9radar.radar.domain.AbstractIntegrationTests;


class ProductIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private ProductService productService;

  @Test
  @WithMockUser
  public void shouldGetTechnologies() {
    // Create product
    ProductDto productDto = new ProductDto();
    productDto.setId(null);
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
            .jsonPath("$").isArray()
            .jsonPath("$[0].id").isEqualTo(productDto.getId())
            .jsonPath("$[0].title").isEqualTo(productDto.getTitle())
            .jsonPath("$[0].description").isEqualTo(productDto.getDescription());

    productService.deleteById(productDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldGetProduct() {
    // Create product
    ProductDto productDto = new ProductDto();
    productDto.setId(null);
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
            .jsonPath("$.title").isEqualTo(productDto.getTitle())
            .jsonPath("$.description").isEqualTo(productDto.getDescription());

    productService.deleteById(productDto.getId());
  }

  @Test
  @WithMockUser
  public void shouldCreateProduct() throws Exception {
    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setTitle("My product");
    productDto.setDescription("My product description");
    productDto = productService.save(productDto);

    webTestClient.post().uri("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(productDto), ProductDto.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$").isNotEmpty()
            .jsonPath("$").isMap()
            .jsonPath("$.id").isEqualTo(productDto.getId())
            .jsonPath("$.title").isEqualTo(productDto.getTitle())
            .jsonPath("$.description").isEqualTo(productDto.getDescription());
    productService.deleteById(productDto.getId());
  }


  @Test
  @WithMockUser
  public void shouldUpdateProduct() throws Exception {
    ProductDto productDto = new ProductDto();
    productDto.setId(null);
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

    productService.deleteById(productDto.getId());
  }


  @Test
  @WithMockUser
  public void shouldDeleteProduct() throws Exception {
    ProductDto productDto = new ProductDto();
    productDto.setId(null);
    productDto.setTitle("My product");
    productDto.setDescription("My product description");
    productDto = productService.save(productDto);

    webTestClient.delete().uri("/api/v1/products/{id}", productDto.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();
  }
}
