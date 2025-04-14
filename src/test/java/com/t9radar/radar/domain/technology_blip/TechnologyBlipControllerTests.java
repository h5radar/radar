package com.t9radar.radar.domain.technology_blip;

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
import com.t9radar.radar.domain.ring.RingDto;
import com.t9radar.radar.domain.segment.SegmentDto;
import com.t9radar.radar.domain.technology.TechnologyDto;

@WebMvcTest(TechnologyBlipController.class)
public class TechnologyBlipControllerTests extends AbstractControllerTests {
  @MockitoBean
  private TechnologyBlipService technologyBlipService;

  @Test
  @WithMockUser
  public void shouldGetTechnologyBlips() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(1L);

    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(4L);

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(2L);

    final RingDto ringDto = new RingDto();
    ringDto.setId(3L);

    final TechnologyBlipDto technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(10L);
    technologyBlipDto.setRadarId(radarDto.getId());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setSegmentId(segmentDto.getId());

    Page<TechnologyBlipDto> technologyBlipDtoPage = new PageImpl<>(Arrays.asList(technologyBlipDto));
    Mockito.when(technologyBlipService.findAll(any(), any())).thenReturn(technologyBlipDtoPage);

    mockMvc.perform(get("/api/v1/technology_blips").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(technologyBlipDtoPage.getContent().size())))
        .andExpect(jsonPath("$[0].id", equalTo(technologyBlipDto.getId()), Long.class))
        .andExpect(jsonPath("$[0].radar_id", equalTo(technologyBlipDto.getRadarId()), Long.class))
        .andExpect(jsonPath("$[0].technology_id", equalTo(technologyBlipDto.getTechnologyId()), Long.class))
        .andExpect(jsonPath("$[0].segment_id", equalTo(technologyBlipDto.getSegmentId()), Long.class))
        .andExpect(jsonPath("$[0].ring_id", equalTo(technologyBlipDto.getRingId()), Long.class));
  }
}
