package com.h5radar.radar.maturity;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.AbstractControllerTests;
import com.h5radar.radar.radar_user.RadarUserDto;

@WebMvcTest(MaturityController.class)
public class MaturityControllerTests extends AbstractControllerTests {
  @MockitoBean
  private MaturityService maturityService;

  @Test
  public void shouldGetMaturities() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("My title");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Page<MaturityDto> maturityDtoPage = new PageImpl<>(Arrays.asList(maturityDto));
    Mockito.when(maturityService.findAll(any(), any())).thenReturn(maturityDtoPage);

    mockMvc.perform(get("/api/v1/maturities")
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(maturityDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(maturityDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user.id", equalTo(maturityDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user.sub", equalTo(maturityDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.content[0].title", equalTo(maturityDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(maturityDto.getDescription())))
        .andExpect(jsonPath("$.content[0].position", equalTo(maturityDto.getPosition())))
        .andExpect(jsonPath("$.content[0].color", equalTo(maturityDto.getColor())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(maturityService).findAll(any(), any());
  }

  /*
  TODO: remove it
  @Test
  public void shouldGetMaturities() throws Exception {
    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);
    // maturityDto.setRadarUserId(radarDto.getId());
    maturityDto.setTitle("My title");
    maturityDto.setDescription("My description");

    Page<MaturityDto> maturityDtoPage = new PageImpl<>(Arrays.asList(maturityDto));
    Mockito.when(maturityService.findAll(any(), any())).thenReturn(maturityDtoPage);

    mockMvc.perform(get("/api/v1/maturities").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(maturityDtoPage.getContent().size())))
        .andExpect(jsonPath("$[0].id", equalTo(maturityDto.getId()), Long.class))
        // .andExpect(jsonPath("$[0].radar_id", equalTo(maturityDto.getRadarId()), Long.class))
        .andExpect(jsonPath("$[0].title", equalTo(maturityDto.getTitle())))
        .andExpect(jsonPath("$[0].description", equalTo(maturityDto.getDescription())))
        .andExpect(jsonPath("$[0].color", equalTo(maturityDto.getColor())));

    Mockito.verify(maturityService).findAll(any(), any());
  }
   */

  public void shouldGetMaturitiesWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetMaturitiesWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  public void shouldFailToGetMaturitiesDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/maturities").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  public void shouldGetMaturity() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("My title");
    maturityDto.setDescription("My description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");


    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(maturityService.findById(any())).thenReturn(Optional.of(maturityDto));

    mockMvc.perform(get("/api/v1/maturities/{id}", maturityDto.getId())
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(maturityDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.id", equalTo(maturityDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.sub", equalTo(maturityDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.title", equalTo(maturityDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(maturityDto.getDescription())))
        .andExpect(jsonPath("$.position", equalTo(maturityDto.getPosition())))
        .andExpect(jsonPath("$.color", equalTo(maturityDto.getColor())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(maturityService).findById(maturityDto.getId());
  }

  @Test
  public void shouldFailToGetMaturityDueToUnauthorized() throws Exception {
    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);

    mockMvc.perform(get("/api/v1/maturities/{id}", maturityDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetMaturityDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  public void shouldCreateMaturity() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("My maturity");
    maturityDto.setDescription("My maturity description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(maturityService.save(any())).thenReturn(maturityDto);

    mockMvc.perform(post("/api/v1/maturities")
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(maturityDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(maturityDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.id", equalTo(maturityDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.sub", equalTo(maturityDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.title", equalTo(maturityDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(maturityDto.getDescription())))
        .andExpect(jsonPath("$.position", equalTo(maturityDto.getPosition())))
        .andExpect(jsonPath("$.color", equalTo(maturityDto.getColor())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(maturityService).save(any());
  }

  @Test
  public void shouldFailToCreateMaturityDueToUnauthorized() throws Exception {
    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);

    mockMvc.perform(post("/api/v1/maturities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(maturityDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreateMaturityDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateMaturityDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  public void shouldUpdateMaturity() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("My maturity");
    maturityDto.setDescription("My maturity description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(maturityService.findById(any())).thenReturn(Optional.of(maturityDto));
    Mockito.when(maturityService.save(any())).thenReturn(maturityDto);

    mockMvc.perform(put("/api/v1/maturities/{id}", maturityDto.getId())
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(maturityDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(maturityService).findById(maturityDto.getId());
    Mockito.verify(maturityService).save(any());
  }

  @Test
  public void shouldFailToUpdateMaturityDueToUnauthorized() throws Exception {
    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);

    mockMvc.perform(put("/api/v1/maturities/{id}", maturityDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(maturityDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdateMaturityDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateMaturityDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateMaturityDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  public void shouldDeleteMaturity() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);
    maturityDto.setRadarUserDto(radarUserDto);
    maturityDto.setTitle("My maturity");
    maturityDto.setDescription("My maturity description");
    maturityDto.setPosition(0);
    maturityDto.setColor("#CCCCCC");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(maturityService.findById(any())).thenReturn(Optional.of(maturityDto));
    Mockito.doAnswer((i) -> null).when(maturityService).deleteById(any());

    mockMvc.perform(delete("/api/v1/maturities/{id}", maturityDto.getId())
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(maturityService).findById(maturityDto.getId());
    Mockito.verify(maturityService).deleteById(maturityDto.getId());
  }

  @Test
  public void shouldFailToDeleteMaturityDueToUnauthorized() throws Exception {
    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(10L);

    mockMvc.perform(delete("/api/v1/maturities/{id}", maturityDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteMaturityDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  @Test
  public void shouldSeedMaturities() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(maturityService.countByRadarUserId(any())).thenReturn(0L);
    Mockito.doAnswer((i) -> null).when(maturityService).seed(any());

    mockMvc.perform(post("/api/v1/maturities/seed")
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(maturityService).countByRadarUserId(radarUserDto.getId());
    Mockito.verify(maturityService).seed(radarUserDto.getId());
  }

  @Test
  public void shouldFailToSeedMaturitiesDueToUnauthorized() throws Exception {
    mockMvc.perform(post("/api/v1/maturities/seed")
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }
}
