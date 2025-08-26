package com.h5radar.radar.compliance;

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

@WebMvcTest(ComplianceController.class)
public class ComplianceControllerTests extends AbstractControllerTests {
  @MockitoBean
  private ComplianceService complianceService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetCompliances() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Page<ComplianceDto> complianceDtoPage = new PageImpl<>(Arrays.asList(complianceDto));
    Mockito.when(complianceService.findAll(any(), any())).thenReturn(complianceDtoPage);

    mockMvc.perform(get("/api/v1/compliances").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(complianceDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(complianceDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user_id", equalTo(complianceDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.content[0].title", equalTo(complianceDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(complianceDto.getDescription())))
        .andExpect(jsonPath("$.content[0].active", equalTo(complianceDto.isActive())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(complianceService).findAll(any(), any());
  }

  public void shouldGetCompliancesWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetCompliancesWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetCompliancesDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/compliances").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetCompliance() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My title");
    complianceDto.setDescription("My description");
    complianceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(complianceService.findById(any())).thenReturn(Optional.of(complianceDto));

    mockMvc.perform(get("/api/v1/compliances/{id}", complianceDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(complianceDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user_id", equalTo(complianceDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(complianceDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(complianceDto.getDescription())))
        .andExpect(jsonPath("$.active", equalTo(complianceDto.isActive())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(complianceService).findById(complianceDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetComplianceDueToUnauthorized() throws Exception {
    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);

    mockMvc.perform(get("/api/v1/compliances/{id}", complianceDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetComplianceDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateCompliance() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(complianceService.save(any())).thenReturn(complianceDto);

    mockMvc.perform(post("/api/v1/compliances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(complianceDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(complianceDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user_id", equalTo(complianceDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(complianceDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(complianceDto.getDescription())))
        .andExpect(jsonPath("$.active", equalTo(complianceDto.isActive())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(complianceService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToCreateComplianceDueToUnauthorized() throws Exception {
    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);

    mockMvc.perform(post("/api/v1/compliances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(complianceDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreateComplianceDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateComplianceDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateCompliance() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(complianceService.findById(any())).thenReturn(Optional.of(complianceDto));
    Mockito.when(complianceService.save(any())).thenReturn(complianceDto);

    mockMvc.perform(put("/api/v1/compliances/{id}", complianceDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(complianceDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(complianceService).findById(complianceDto.getId());
    Mockito.verify(complianceService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToUpdateComplianceDueToUnauthorized() throws Exception {
    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);

    mockMvc.perform(put("/api/v1/compliances/{id}", complianceDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(complianceDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdateComplianceDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateComplianceDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateComplianceDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteCompliance() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);
    complianceDto.setRadarUserId(radarUserDto.getId());
    complianceDto.setTitle("My compliance");
    complianceDto.setDescription("My compliance description");
    complianceDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(complianceService.findById(any())).thenReturn(Optional.of(complianceDto));
    Mockito.doAnswer((i) -> null).when(complianceService).deleteById(any());

    mockMvc.perform(delete("/api/v1/compliances/{id}", complianceDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(complianceService).findById(complianceDto.getId());
    Mockito.verify(complianceService).deleteById(complianceDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToDeleteComplianceDueToUnauthorized() throws Exception {
    final ComplianceDto complianceDto = new ComplianceDto();
    complianceDto.setId(10L);

    mockMvc.perform(delete("/api/v1/compliances/{id}", complianceDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteComplianceDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedCompliances() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(complianceService.countByRadarUserId(any())).thenReturn(0L);
    Mockito.doAnswer((i) -> null).when(complianceService).seed(any());

    mockMvc.perform(post("/api/v1/compliances/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(complianceService).countByRadarUserId(radarUserDto.getId());
    Mockito.verify(complianceService).seed(radarUserDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToSeedCompliancesDueToUnauthorized() throws Exception {
    mockMvc.perform(post("/api/v1/compliances/seed/{radar_user_id}")
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }
}
