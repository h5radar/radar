package com.h5radar.radar.domain.technology;

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

@WebMvcTest(TechnologyController.class)
public class TechnologyControllerTests extends AbstractControllerTests {

  @MockitoBean
  private TechnologyService technologyService;

  @Test
  @WithMockUser
  public void shouldGetTechnologies() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);
    technologyDto.setTitle("My title");
    technologyDto.setDescription("My description");
    technologyDto.setWebsite("My website");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    Page<TechnologyDto> technologyDtoPage = new PageImpl<>(Arrays.asList(technologyDto));
    Mockito.when(technologyService.findAll(any(), any())).thenReturn(technologyDtoPage);

    mockMvc.perform(get("/api/v1/technologies").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(technologyDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(technologyDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].title", equalTo(technologyDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(technologyDto.getDescription())))
        .andExpect(jsonPath("$.content[0].website", equalTo(technologyDto.getWebsite())))
        .andExpect(jsonPath("$.content[0].moved", equalTo(technologyDto.getMoved()), int.class))
        .andExpect(jsonPath("$.content[0].active", equalTo(technologyDto.isActive())));

    Mockito.verify(technologyService).findAll(any(), any());
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
    mockMvc.perform(get("/api/v1/technologies").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser
  public void shouldGetTechnology() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);
    technologyDto.setTitle("My title");
    technologyDto.setDescription("My description");
    technologyDto.setWebsite("My website");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    Mockito.when(technologyService.findById(any())).thenReturn(Optional.of(technologyDto));

    mockMvc.perform(get("/api/v1/technologies/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(technologyDto.getId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(technologyDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(technologyDto.getDescription())))
        .andExpect(jsonPath("$.website", equalTo(technologyDto.getWebsite())))
        .andExpect(jsonPath("$.moved", equalTo(technologyDto.getMoved()), int.class))
        .andExpect(jsonPath("$.active", equalTo(technologyDto.isActive())));

    Mockito.verify(technologyService).findById(technologyDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetTechnologyDueToUnauthorized() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);

    mockMvc.perform(get("/api/v1/technologies/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetTechnologyDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldCreateTechnology() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    Mockito.when(technologyService.save(any())).thenReturn(technologyDto);

    mockMvc.perform(post("/api/v1/technologies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(technologyDto.getId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(technologyDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(technologyDto.getDescription())))
        .andExpect(jsonPath("$.website", equalTo(technologyDto.getWebsite())))
        .andExpect(jsonPath("$.moved", equalTo(technologyDto.getMoved()), int.class))
        .andExpect(jsonPath("$.active", equalTo(technologyDto.isActive())));

    Mockito.verify(technologyService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToCreateTechnologyDueToUnauthorized() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);

    mockMvc.perform(post("/api/v1/technologies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreateTechnologyDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateTechnologyDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldUpdateTechnology() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    Mockito.when(technologyService.findById(any())).thenReturn(Optional.of(technologyDto));
    Mockito.when(technologyService.save(any())).thenReturn(technologyDto);

    mockMvc.perform(put("/api/v1/technologies/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(technologyService).findById(technologyDto.getId());
    Mockito.verify(technologyService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToUpdateTechnologyDueToUnauthorized() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);

    mockMvc.perform(put("/api/v1/technologies/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdateTechnologyDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateTechnologyDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateTechnologyDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldDeleteTechnology() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);
    technologyDto.setWebsite("My website");
    technologyDto.setTitle("My technology");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(0);
    technologyDto.setActive(true);

    Mockito.when(technologyService.findById(any())).thenReturn(Optional.of(technologyDto));
    Mockito.doAnswer((i) -> null).when(technologyService).deleteById(any());

    mockMvc.perform(delete("/api/v1/technologies/{id}", technologyDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(technologyService).findById(technologyDto.getId());
    Mockito.verify(technologyService).deleteById(technologyDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToDeleteTechnologyDueToUnauthorized() throws Exception {
    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(10L);

    mockMvc.perform(delete("/api/v1/technologies/{id}", technologyDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteTechnologyDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }
}
