package com.t9radar.radar.domain.ring;

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

import com.t9radar.radar.domain.AbstractControllerTests;
import com.t9radar.radar.domain.radar.RadarDto;

@WebMvcTest(RingController.class)
public class RingControllerTests extends AbstractControllerTests {
  @MockitoBean
  private RingService ringService;

  @Test
  @WithMockUser
  public void shouldGetRings() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(12L);
    radarDto.setTitle("My radar");
    radarDto.setDescription("My radar description");

    final RingDto ringDto = new RingDto();
    ringDto.setId(10L);
    ringDto.setRadarId(radarDto.getId());
    ringDto.setTitle("My title");
    ringDto.setDescription("My description");

    Page<RingDto> ringDtoPage = new PageImpl<>(Arrays.asList(ringDto));
    Mockito.when(ringService.findAll(any(), any())).thenReturn(ringDtoPage);

    mockMvc.perform(get("/api/v1/rings").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(ringDtoPage.getContent().size())))
        .andExpect(jsonPath("$[0].id", equalTo(ringDto.getId()), Long.class))
        .andExpect(jsonPath("$[0].radar_id", equalTo(ringDto.getRadarId()), Long.class))
        .andExpect(jsonPath("$[0].title", equalTo(ringDto.getTitle())))
        .andExpect(jsonPath("$[0].description", equalTo(ringDto.getDescription())))
        .andExpect(jsonPath("$[0].color", equalTo(ringDto.getColor())));

    Mockito.verify(ringService).findAll(any(), any());
  }
}
