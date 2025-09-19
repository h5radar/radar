package com.h5radar.radar.domain;

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

@WebMvcTest(DomainController.class)
public class DomainControllerTests extends AbstractControllerTests {
  @MockitoBean
  private DomainService domainService;

  @Test
  public void shouldGetDomains() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);
    domainDto.setRadarUserDto(radarUserDto);
    domainDto.setTitle("My title");
    domainDto.setDescription("My description");
    domainDto.setPosition(0);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Page<DomainDto> domainDtoPage = new PageImpl<>(Arrays.asList(domainDto));
    Mockito.when(domainService.findAll(any(), any())).thenReturn(domainDtoPage);

    mockMvc.perform(get("/api/v1/domains")
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(domainDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(domainDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user.id", equalTo(domainDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.content[0].radar_user.sub", equalTo(domainDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.content[0].title", equalTo(domainDto.getTitle())))
        .andExpect(jsonPath("$.content[0].description", equalTo(domainDto.getDescription())))
        .andExpect(jsonPath("$.content[0].position", equalTo(domainDto.getPosition())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(domainService).findAll(any(), any());
  }

  /*
  TODO: remove it
  @Test
  public void shouldGetDomains() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(1L);

    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);
    domainDto.setRadarId(radarDto.getId());
    domainDto.setRadarTitle(radarDto.getTitle());
    domainDto.setTitle("My title");
    domainDto.setDescription("My description");
    domainDto.setPosition(1);

    Page<DomainDto> domainDtoPage = new PageImpl<>(Arrays.asList(domainDto));
    Mockito.when(domainService.findAll(any(), any())).thenReturn(domainDtoPage);

    mockMvc.perform(get("/api/v1/domains").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(domainDtoPage.getContent().size())))
        .andExpect(jsonPath("$[0].id", equalTo(domainDto.getId()), Long.class))
        .andExpect(jsonPath("$[0].radar_id", equalTo(domainDto.getRadarId()), Long.class))
        .andExpect(jsonPath("$[0].title", equalTo(domainDto.getTitle())))
        .andExpect(jsonPath("$[0].description", equalTo(domainDto.getDescription())))
        .andExpect(jsonPath("$[0].position", equalTo(domainDto.getPosition()), int.class));
  }
  */

  public void shouldGetDomainsWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetDomainsWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  public void shouldFailToGetDomainsDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/domains").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  public void shouldGetDomain() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);
    domainDto.setRadarUserDto(radarUserDto);
    domainDto.setTitle("My title");
    domainDto.setDescription("My description");
    domainDto.setPosition(0);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(domainService.findById(any())).thenReturn(Optional.of(domainDto));

    mockMvc.perform(get("/api/v1/domains/{id}", domainDto.getId())
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(domainDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.id", equalTo(domainDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.sub", equalTo(domainDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.title", equalTo(domainDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(domainDto.getDescription())))
        .andExpect(jsonPath("$.position", equalTo(domainDto.getPosition())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(domainService).findById(domainDto.getId());
  }

  @Test
  public void shouldFailToGetDomainDueToUnauthorized() throws Exception {
    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);

    mockMvc.perform(get("/api/v1/domains/{id}", domainDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetDomainDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  public void shouldCreateDomain() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);
    domainDto.setRadarUserDto(radarUserDto);
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(domainService.save(any())).thenReturn(domainDto);

    mockMvc.perform(post("/api/v1/domains")
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(domainDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(domainDto.getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.id", equalTo(domainDto.getRadarUserDto().getId()), Long.class))
        .andExpect(jsonPath("$.radar_user.sub", equalTo(domainDto.getRadarUserDto().getSub())))
        .andExpect(jsonPath("$.title", equalTo(domainDto.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(domainDto.getDescription())))
        .andExpect(jsonPath("$.position", equalTo(domainDto.getPosition())));

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(domainService).save(any());
  }

  @Test
  public void shouldFailToCreateDomainDueToUnauthorized() throws Exception {
    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);

    mockMvc.perform(post("/api/v1/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(domainDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreateDomainDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateDomainDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  public void shouldUpdateDomain() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);
    domainDto.setRadarUserDto(radarUserDto);
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(domainService.findById(any())).thenReturn(Optional.of(domainDto));
    Mockito.when(domainService.save(any())).thenReturn(domainDto);

    mockMvc.perform(put("/api/v1/domains/{id}", domainDto.getId())
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(domainDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(domainService).findById(domainDto.getId());
    Mockito.verify(domainService).save(any());
  }

  @Test
  public void shouldFailToUpdateDomainDueToUnauthorized() throws Exception {
    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);

    mockMvc.perform(put("/api/v1/domains/{id}", domainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(domainDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdateDomainDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateDomainDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateDomainDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  public void shouldDeleteDomain() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);
    domainDto.setRadarUserDto(radarUserDto);
    domainDto.setTitle("My domain");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(0);

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(domainService.findById(any())).thenReturn(Optional.of(domainDto));
    Mockito.doAnswer((i) -> null).when(domainService).deleteById(any());

    mockMvc.perform(delete("/api/v1/domains/{id}", domainDto.getId())
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(domainService).findById(domainDto.getId());
    Mockito.verify(domainService).deleteById(domainDto.getId());
  }

  @Test
  public void shouldFailToDeleteDomainDueToUnauthorized() throws Exception {
    final DomainDto domainDto = new DomainDto();
    domainDto.setId(10L);

    mockMvc.perform(delete("/api/v1/domains/{id}", domainDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteDomainDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  @Test
  public void shouldSeedDomains() throws Exception {
    final RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(11L);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");

    Mockito.when(radarUserService.save(any())).thenReturn(radarUserDto);
    Mockito.when(domainService.countByRadarUserId(any())).thenReturn(0L);
    Mockito.doAnswer((i) -> null).when(domainService).seed(any());

    mockMvc.perform(post("/api/v1/domains/seed")
            .with(jwt().jwt(j -> {
              j.claim("sub", "My sub");
              j.claim("preferred_username", "My username");
            }))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(radarUserService).save(any());
    Mockito.verify(domainService).countByRadarUserId(radarUserDto.getId());
    Mockito.verify(domainService).seed(radarUserDto.getId());
  }

  @Test
  public void shouldFailToSeedDomainsDueToUnauthorized() throws Exception {
    mockMvc.perform(post("/api/v1/domains/seed")
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }
}
