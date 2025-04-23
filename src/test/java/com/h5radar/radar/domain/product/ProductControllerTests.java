package com.h5radar.radar.domain.product;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractControllerTests;

@WebMvcTest(ProductController.class)
public class ProductControllerTests extends AbstractControllerTests {

  @MockitoBean
  private ProductService productService;

  @Test
  @WithMockUser
  public void shouldGetTechnologies() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);
    productDto.setTitle("My title");
    productDto.setDescription("My description");

    Page<ProductDto> productDtoPage = new PageImpl<>(Arrays.asList(productDto));
    Mockito.when(productService.findAll(any(), any())).thenReturn(productDtoPage);

    mockMvc.perform(get("/api/v1/products").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(productDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(productDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].title", equalTo(productDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(productDto.getDescription())));

    Mockito.verify(productService).findAll(any(), any());
  }

  public void shouldGetTechnologiesWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetTechnologiesWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetTechnologiesDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/products").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser
  public void shouldGetProduct() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);
    productDto.setTitle("My title");
    productDto.setDescription("My description");

    Mockito.when(productService.findById(any())).thenReturn(Optional.of(productDto));

    mockMvc.perform(get("/api/v1/products/{id}", productDto.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.id", equalTo(productDto.getId()), Long.class))
            .andExpect(jsonPath("$.title", equalTo(productDto.getTitle())))
            .andExpect(jsonPath("$.description", equalTo(productDto.getDescription())));

    Mockito.verify(productService).findById(productDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetProductDueToUnauthorized() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);

    mockMvc.perform(get("/api/v1/products/{id}", productDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetProductDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldCreateProduct() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);
    productDto.setTitle("My product");
    productDto.setDescription("My product description");

    Mockito.when(productService.save(any())).thenReturn(productDto);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(productDto.getId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(productDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(productDto.getDescription())));

    Mockito.verify(productService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToCreateProductDueToUnauthorized() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }


  public void shouldFailToCreateProductDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateProductDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldUpdateProduct() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);
    productDto.setTitle("My product");
    productDto.setDescription("My product description");

    Mockito.when(productService.findById(any())).thenReturn(Optional.of(productDto));
    Mockito.when(productService.save(any())).thenReturn(productDto);

    mockMvc.perform(put("/api/v1/products/{id}", productDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(productService).findById(productDto.getId());
    Mockito.verify(productService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToUpdateProductDueToUnauthorized() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);
    mockMvc.perform(put("/api/v1/products/{id}", productDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToUpdateProductDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateProductDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateProductDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldDeleteProduct() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);
    productDto.setTitle("My product");
    productDto.setDescription("My product description");

    Mockito.when(productService.findById(any())).thenReturn(Optional.of(productDto));
    Mockito.doAnswer((i) -> null).when(productService).deleteById(any());

    mockMvc.perform(delete("/api/v1/products/{id}", productDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(productService).findById(productDto.getId());
    Mockito.verify(productService).deleteById(productDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToDeleteProductDueToUnauthorized() throws Exception {
    final ProductDto productDto = new ProductDto();
    productDto.setId(10L);

    mockMvc.perform(delete("/api/v1/products/{id}", productDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteProductDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }
}
