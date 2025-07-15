package com.h5radar.radar.domain.maturity;

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

@WebMvcTest(MaturityController.class)
public class MaturityControllerTests extends AbstractControllerTests {
  @MockitoBean
  private MaturityService maturityService;

  @Test
  @WithMockUser(value = "My sub")
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
}
