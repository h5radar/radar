package com.h5radar.radar.domain.practice;

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

@WebMvcTest(PracticeController.class)
public class PracticeControllerTests extends AbstractControllerTests {

  @MockitoBean
  private PracticeService practiceService;

  @Test
  @WithMockUser
  public void shouldGetPractices() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserId(15L);
    practiceDto.setTitle("My title");
    practiceDto.setDescription("My description");
    practiceDto.setWebsite("My website");
    practiceDto.setMoved(1);
    practiceDto.setActive(true);

    Page<PracticeDto> practiceDtoPage = new PageImpl<>(Arrays.asList(practiceDto));
    Mockito.when(practiceService.findAll(any(), any())).thenReturn(practiceDtoPage);

    mockMvc.perform(get("/api/v1/practices").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(practiceDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(practiceDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user_id", equalTo(practiceDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.content[0].title", equalTo(practiceDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(practiceDto.getDescription())))
        .andExpect(jsonPath("$.content[0].website", equalTo(practiceDto.getWebsite())))
        .andExpect(jsonPath("$.content[0].moved", equalTo(practiceDto.getMoved()), int.class))
        .andExpect(jsonPath("$.content[0].active", equalTo(practiceDto.isActive())));

    Mockito.verify(practiceService).findAll(any(), any());
  }

  public void shouldGetPracticesWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetPracticesWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetPracticesDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/practices").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser
  public void shouldGetPractice() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserId(15L);
    practiceDto.setTitle("My title");
    practiceDto.setDescription("My description");
    practiceDto.setWebsite("My website");
    practiceDto.setMoved(1);
    practiceDto.setActive(true);

    Mockito.when(practiceService.findById(any())).thenReturn(Optional.of(practiceDto));

    mockMvc.perform(get("/api/v1/practices/{id}", practiceDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(practiceDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user_id", equalTo(practiceDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(practiceDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(practiceDto.getDescription())))
        .andExpect(jsonPath("$.website", equalTo(practiceDto.getWebsite())))
        .andExpect(jsonPath("$.moved", equalTo(practiceDto.getMoved()), int.class))
        .andExpect(jsonPath("$.active", equalTo(practiceDto.isActive())));

    Mockito.verify(practiceService).findById(practiceDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetPracticeDueToUnauthorized() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);

    mockMvc.perform(get("/api/v1/practices/{id}", practiceDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetPracticeDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldCreatePractice() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserId(15L);
    practiceDto.setWebsite("My website");
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setMoved(0);
    practiceDto.setActive(true);

    Mockito.when(practiceService.save(any())).thenReturn(practiceDto);

    mockMvc.perform(post("/api/v1/practices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(practiceDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(practiceDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user_id", equalTo(practiceDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(practiceDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(practiceDto.getDescription())))
        .andExpect(jsonPath("$.website", equalTo(practiceDto.getWebsite())))
        .andExpect(jsonPath("$.moved", equalTo(practiceDto.getMoved()), int.class))
        .andExpect(jsonPath("$.active", equalTo(practiceDto.isActive())));

    Mockito.verify(practiceService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToCreatePracticeDueToUnauthorized() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);

    mockMvc.perform(post("/api/v1/practices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(practiceDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreatePracticeDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreatePracticeDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldUpdatePractice() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserId(15L);
    practiceDto.setWebsite("My website");
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setMoved(0);
    practiceDto.setActive(true);

    Mockito.when(practiceService.findById(any())).thenReturn(Optional.of(practiceDto));
    Mockito.when(practiceService.save(any())).thenReturn(practiceDto);

    mockMvc.perform(put("/api/v1/practices/{id}", practiceDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(practiceDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(practiceService).findById(practiceDto.getId());
    Mockito.verify(practiceService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToUpdatePracticeDueToUnauthorized() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);

    mockMvc.perform(put("/api/v1/practices/{id}", practiceDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(practiceDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdatePracticeDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdatePracticeDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdatePracticeDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldDeletePractice() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserId(15L);
    practiceDto.setWebsite("My website");
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setMoved(0);
    practiceDto.setActive(true);

    Mockito.when(practiceService.findById(any())).thenReturn(Optional.of(practiceDto));
    Mockito.doAnswer((i) -> null).when(practiceService).deleteById(any());

    mockMvc.perform(delete("/api/v1/practices/{id}", practiceDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(practiceService).findById(practiceDto.getId());
    Mockito.verify(practiceService).deleteById(practiceDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToDeletePracticeDueToUnauthorized() throws Exception {
    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);

    mockMvc.perform(delete("/api/v1/practices/{id}", practiceDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeletePracticeDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }
}
