package com.h5radar.radar.domain.radar_user;

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

@WebMvcTest(RadarUserController.class)
public class RadarUserControllerTests extends AbstractControllerTests {

  @Test
  @WithMockUser
  public void shouldGetRadarUsers() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Page<RadarUserDto> radarUserDtoPage = new PageImpl<>(Arrays.asList(radarUserDto));
    Mockito.when(radarUserService.findAll(any(), any())).thenReturn(radarUserDtoPage);

    mockMvc.perform(get("/api/v1/radar-users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(radarUserDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(radarUserDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].sub", equalTo(radarUserDto.getSub())))
        .andExpect(jsonPath("$.content[0].username", equalTo(radarUserDto.getUsername())));

    Mockito.verify(radarUserService).findAll(any(), any());
  }

  public void shouldGetRadarUsersWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetRadarUsersWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetRadarUsersDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/radar-users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser
  public void shouldGetRadarUser() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.findById(any())).thenReturn(Optional.of(radarUserDto));

    mockMvc.perform(get("/api/v1/radar-users/{id}", radarUserDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(radarUserDto.getId()), Long.class))
        .andExpect(jsonPath("$.sub", equalTo(radarUserDto.getSub())))
        .andExpect(jsonPath("$.username", equalTo(radarUserDto.getUsername())));

    Mockito.verify(radarUserService).findById(radarUserDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetRadarUserDueToUnauthorized() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);

    mockMvc.perform(get("/api/v1/radar-users/{id}", radarUserDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetRadarUserDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  /*
  @Test
  @WithMockUser
  public void shouldCreateRadarUser() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);

    mockMvc.perform(post("/api/v1/radar-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(radarUserDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(radarUserDto.getId()), Long.class))
        .andExpect(jsonPath("$.sub", equalTo(radarUserDto.getSub())))
        .andExpect(jsonPath("$.username", equalTo(radarUserDto.getUsername())));

    Mockito.verify(radarUserService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToCreateRadarUserDueToUnauthorized() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);

    mockMvc.perform(post("/api/v1/radar-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(radarUserDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreateRadarUserDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateRadarUserDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldUpdateRadarUser() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.findById(any())).thenReturn(Optional.of(radarUserDto));
    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);

    mockMvc.perform(put("/api/v1/radar-users/{id}", radarUserDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(radarUserDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).findById(radarUserDto.getId());
    Mockito.verify(radarUserService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToUpdateRadarUserDueToUnauthorized() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);

    mockMvc.perform(put("/api/v1/radar-users/{id}", radarUserDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(radarUserDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdateRadarUserDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateRadarUserDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateRadarUserDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldDeleteRadarUser() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.findById(any())).thenReturn(Optional.of(radarUserDto));
    Mockito.doAnswer((i) -> null).when(radarUserService).deleteById(any());

    mockMvc.perform(delete("/api/v1/radar-users/{id}", radarUserDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(radarUserService).findById(radarUserDto.getId());
    Mockito.verify(radarUserService).deleteById(radarUserDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToDeleteRadarUserDueToUnauthorized() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);

    mockMvc.perform(delete("/api/v1/radar-users/{id}", radarUserDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteRadarUserDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }
   */
}
