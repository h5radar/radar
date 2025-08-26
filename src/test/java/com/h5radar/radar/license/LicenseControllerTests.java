package com.h5radar.radar.license;

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

@WebMvcTest(LicenseController.class)
public class LicenseControllerTests extends AbstractControllerTests {
  @MockitoBean
  private LicenseService licenseService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetLicenses() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My title");
    licenseDto.setDescription("My description");
    licenseDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Page<LicenseDto> licenseDtoPage = new PageImpl<>(Arrays.asList(licenseDto));
    Mockito.when(licenseService.findAll(any(), any())).thenReturn(licenseDtoPage);

    mockMvc.perform(get("/api/v1/licenses").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(licenseDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(licenseDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user_id", equalTo(licenseDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.content[0].title", equalTo(licenseDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(licenseDto.getDescription())))
        .andExpect(jsonPath("$.content[0].active", equalTo(licenseDto.isActive())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(licenseService).findAll(any(), any());
  }

  public void shouldGetLicensesWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetLicensesWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetLicensesDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/licenses").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetLicense() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My title");
    licenseDto.setDescription("My description");
    licenseDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(licenseService.findById(any())).thenReturn(Optional.of(licenseDto));

    mockMvc.perform(get("/api/v1/licenses/{id}", licenseDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(licenseDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user_id", equalTo(licenseDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(licenseDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(licenseDto.getDescription())))
        .andExpect(jsonPath("$.active", equalTo(licenseDto.isActive())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(licenseService).findById(licenseDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetLicenseDueToUnauthorized() throws Exception {
    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);

    mockMvc.perform(get("/api/v1/licenses/{id}", licenseDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetLicenseDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldCreateLicense() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(licenseService.save(any())).thenReturn(licenseDto);

    mockMvc.perform(post("/api/v1/licenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(licenseDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(licenseDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user_id", equalTo(licenseDto.getRadarUserId()), Long.class))
        .andExpect(jsonPath("$.title", equalTo(licenseDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(licenseDto.getDescription())))
        .andExpect(jsonPath("$.active", equalTo(licenseDto.isActive())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(licenseService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToCreateLicenseDueToUnauthorized() throws Exception {
    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);

    mockMvc.perform(post("/api/v1/licenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(licenseDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreateLicenseDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateLicenseDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldUpdateLicense() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(licenseService.findById(any())).thenReturn(Optional.of(licenseDto));
    Mockito.when(licenseService.save(any())).thenReturn(licenseDto);

    mockMvc.perform(put("/api/v1/licenses/{id}", licenseDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(licenseDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(licenseService).findById(licenseDto.getId());
    Mockito.verify(licenseService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToUpdateLicenseDueToUnauthorized() throws Exception {
    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);

    mockMvc.perform(put("/api/v1/licenses/{id}", licenseDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(licenseDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdateLicenseDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateLicenseDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateLicenseDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser(value = "My sub")
  public void shouldDeleteLicense() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);
    licenseDto.setRadarUserId(radarUserDto.getId());
    licenseDto.setTitle("My license");
    licenseDto.setDescription("My license description");
    licenseDto.setActive(true);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(licenseService.findById(any())).thenReturn(Optional.of(licenseDto));
    Mockito.doAnswer((i) -> null).when(licenseService).deleteById(any());

    mockMvc.perform(delete("/api/v1/licenses/{id}", licenseDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(licenseService).findById(licenseDto.getId());
    Mockito.verify(licenseService).deleteById(licenseDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToDeleteLicenseDueToUnauthorized() throws Exception {
    final LicenseDto licenseDto = new LicenseDto();
    licenseDto.setId(10L);

    mockMvc.perform(delete("/api/v1/licenses/{id}", licenseDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteLicenseDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedLicenses() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(licenseService.countByRadarUserId(any())).thenReturn(0L);
    Mockito.doAnswer((i) -> null).when(licenseService).seed(any());

    mockMvc.perform(post("/api/v1/licenses/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(licenseService).countByRadarUserId(radarUserDto.getId());
    Mockito.verify(licenseService).seed(radarUserDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToSeedLicensesDueToUnauthorized() throws Exception {
    mockMvc.perform(post("/api/v1/licenses/seed/{radar_user_id}")
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }
}
