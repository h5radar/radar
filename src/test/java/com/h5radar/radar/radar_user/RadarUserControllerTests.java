package com.h5radar.radar.radar_user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.h5radar.radar.compliance.ComplianceService;
import com.h5radar.radar.domain.DomainService;
import com.h5radar.radar.license.LicenseService;
import com.h5radar.radar.maturity.MaturityService;
import com.h5radar.radar.practice.PracticeService;
import com.h5radar.radar.technology.TechnologyService;


@WebMvcTest(RadarUserController.class)
public class RadarUserControllerTests extends AbstractControllerTests {

  @MockitoBean
  private ComplianceService complianceService;

  @MockitoBean
  private LicenseService licenseService;

  @MockitoBean
  private PracticeService practiceService;

  @MockitoBean
  private MaturityService maturityService;

  @MockitoBean
  private DomainService domainService;

  @MockitoBean
  private TechnologyService technologyService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetRadarUsers() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
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

    Mockito.verify(radarUserService).save(any());
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
  @WithMockUser(value = "My sub")
  public void shouldGetRadarUser() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(10L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(radarUserService.findById(any())).thenReturn(Optional.of(radarUserDto));

    mockMvc.perform(get("/api/v1/radar-users/{id}", radarUserDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(radarUserDto.getId()), Long.class))
        .andExpect(jsonPath("$.sub", equalTo(radarUserDto.getSub())))
        .andExpect(jsonPath("$.username", equalTo(radarUserDto.getUsername())));

    Mockito.verify(radarUserService).save(any());
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

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedAllWhenEmpty() throws Exception {
    // Arrange: все countByRadarUserId == 0
    Mockito.when(complianceService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(licenseService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(practiceService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(maturityService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(domainService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(technologyService.countByRadarUserId(anyLong())).thenReturn(0L);

    // Act + Assert
    mockMvc.perform(post("/api/v1/radar-users/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isNoContent());

    // Verify
    Mockito.verify(complianceService, times(1)).seed(anyLong());
    Mockito.verify(licenseService, times(1)).seed(anyLong());
    Mockito.verify(practiceService, times(1)).seed(anyLong());
    Mockito.verify(maturityService, times(1)).seed(anyLong());
    Mockito.verify(domainService, times(1)).seed(anyLong());
    Mockito.verify(technologyService, times(1)).seed(anyLong());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldNotSeedIfAlreadyInitializedByCompliance() throws Exception {
    // Arrange
    Mockito.when(complianceService.countByRadarUserId(anyLong())).thenReturn(1L);

    // Act + Assert
    mockMvc.perform(post("/api/v1/radar-users/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isNoContent());

    // Verify
    Mockito.verify(complianceService, never()).seed(anyLong());
    Mockito.verify(licenseService,  never()).seed(anyLong());
    Mockito.verify(practiceService, never()).seed(anyLong());
    Mockito.verify(maturityService, never()).seed(anyLong());
    Mockito.verify(domainService,   never()).seed(anyLong());
    Mockito.verify(technologyService, never()).seed(anyLong());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldSeedOnlyMissingBuckets() throws Exception {
    // Arrange
    Mockito.when(complianceService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(licenseService.countByRadarUserId(anyLong())).thenReturn(2L);
    Mockito.when(practiceService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(maturityService.countByRadarUserId(anyLong())).thenReturn(5L);
    Mockito.when(domainService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.when(technologyService.countByRadarUserId(anyLong())).thenReturn(7L);

    // Act + Assert
    mockMvc.perform(post("/api/v1/radar-users/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isNoContent());

    // Verify
    Mockito.verify(complianceService, never()).seed(anyLong());
    Mockito.verify(licenseService, never()).seed(anyLong());
    Mockito.verify(practiceService, never()).seed(anyLong());
    Mockito.verify(maturityService, never()).seed(anyLong());
    Mockito.verify(domainService,  never()).seed(anyLong());
    Mockito.verify(technologyService, never()).seed(anyLong());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldReturn500WhenSeedThrows() throws Exception {
    // Arrange
    Mockito.when(complianceService.countByRadarUserId(anyLong())).thenReturn(0L);
    Mockito.doThrow(new RuntimeException("boom"))
        .when(complianceService).seed(anyLong());

    // Act + Assert
    mockMvc.perform(post("/api/v1/radar-users/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isInternalServerError());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailSeedDueToUnauthorized() throws Exception {
    mockMvc.perform(post("/api/v1/radar-users/seed")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }
}


