package com.h5radar.radar.domain.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractControllerTests;
import com.h5radar.radar.domain.radar.RadarDto;

@WebMvcTest(DomainController.class)
public class DomainControllerTests extends AbstractControllerTests {
  @MockitoBean
  private DomainService domainService;

  @Test
  @WithMockUser(value = "My sub")
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
}
