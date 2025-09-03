package com.h5radar.radar.practice;

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

import com.h5radar.radar.AbstractControllerTests;
import com.h5radar.radar.radar_user.RadarUserDto;

@WebMvcTest(PracticeController.class)
public class PracticeControllerTests extends AbstractControllerTests {
  @MockitoBean
  private PracticeService practiceService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetPractices() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My title");
    practiceDto.setDescription("My description");
    practiceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Page<PracticeDto> practiceDtoPage = new PageImpl<>(Arrays.asList(practiceDto));
    Mockito.when(practiceService.findAll(any(), any())).thenReturn(practiceDtoPage);

    mockMvc.perform(get("/api/v1/practices").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(practiceDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(practiceDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user.id", equalTo(practiceDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user.sub", equalTo(practiceDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.content[0].title", equalTo(practiceDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(practiceDto.getDescription())))
        .andExpect(jsonPath("$.content[0].active", equalTo(practiceDto.isActive())));

    Mockito.verify(radarUserService).save(any());
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
  @WithMockUser(value = "My sub")
  public void shouldGetPractice() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My title");
    practiceDto.setDescription("My description");
    practiceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(practiceService.findById(any())).thenReturn(Optional.of(practiceDto));

    mockMvc.perform(get("/api/v1/practices/{id}", practiceDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(practiceDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.id", equalTo(practiceDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.sub", equalTo(practiceDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.title", equalTo(practiceDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(practiceDto.getDescription())))
        .andExpect(jsonPath("$.active", equalTo(practiceDto.isActive())));

    Mockito.verify(radarUserService).save(any());
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
  @WithMockUser(value = "My sub")
  public void shouldCreatePractice() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(practiceService.save(any())).thenReturn(practiceDto);

    mockMvc.perform(post("/api/v1/practices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(practiceDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(practiceDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.id", equalTo(practiceDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.sub", equalTo(practiceDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.title", equalTo(practiceDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(practiceDto.getDescription())))
        .andExpect(jsonPath("$.active", equalTo(practiceDto.isActive())));

    Mockito.verify(radarUserService).save(any());
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
  @WithMockUser(value = "My sub")
  public void shouldUpdatePractice() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(practiceService.findById(any())).thenReturn(Optional.of(practiceDto));
    Mockito.when(practiceService.save(any())).thenReturn(practiceDto);

    mockMvc.perform(put("/api/v1/practices/{id}", practiceDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(practiceDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
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
  @WithMockUser(value = "My sub")
  public void shouldDeletePractice() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final PracticeDto practiceDto = new PracticeDto();
    practiceDto.setId(10L);
    practiceDto.setRadarUserDto(radarUserDto);
    practiceDto.setTitle("My practice");
    practiceDto.setDescription("My practice description");
    practiceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(practiceService.findById(any())).thenReturn(Optional.of(practiceDto));
    Mockito.doAnswer((i) -> null).when(practiceService).deleteById(any());

    mockMvc.perform(delete("/api/v1/practices/{id}", practiceDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(radarUserService).save(any());
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

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedPractices() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");


    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(practiceService.countByRadarUserId(any())).thenReturn(0L);
    Mockito.doAnswer((i) -> null).when(practiceService).seed(any());

    mockMvc.perform(post("/api/v1/practices/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(practiceService).countByRadarUserId(radarUserDto.getId());
    Mockito.verify(practiceService).seed(radarUserDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToSeedPracticesDueToUnauthorized() throws Exception {
    mockMvc.perform(post("/api/v1/practices/seed")
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }
}
