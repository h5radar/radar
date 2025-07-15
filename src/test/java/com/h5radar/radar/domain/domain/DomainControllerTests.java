package com.h5radar.radar.domain.segment;

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

@WebMvcTest(SegmentController.class)
public class SegmentControllerTests extends AbstractControllerTests {
  @MockitoBean
  private SegmentService segmentService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetSegments() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(1L);

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(10L);
    segmentDto.setRadarId(radarDto.getId());
    segmentDto.setRadarTitle(radarDto.getTitle());
    segmentDto.setTitle("My title");
    segmentDto.setDescription("My description");
    segmentDto.setPosition(1);

    Page<SegmentDto> segmentDtoPage = new PageImpl<>(Arrays.asList(segmentDto));
    Mockito.when(segmentService.findAll(any(), any())).thenReturn(segmentDtoPage);

    mockMvc.perform(get("/api/v1/segments").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(segmentDtoPage.getContent().size())))
        .andExpect(jsonPath("$[0].id", equalTo(segmentDto.getId()), Long.class))
        .andExpect(jsonPath("$[0].radar_id", equalTo(segmentDto.getRadarId()), Long.class))
        .andExpect(jsonPath("$[0].title", equalTo(segmentDto.getTitle())))
        .andExpect(jsonPath("$[0].description", equalTo(segmentDto.getDescription())))
        .andExpect(jsonPath("$[0].position", equalTo(segmentDto.getPosition()), int.class));
  }
}
