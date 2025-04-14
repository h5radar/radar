package com.t9radar.radar.domain.radar;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.t9radar.radar.domain.AbstractControllerTests;
import com.t9radar.radar.domain.radar_type.RadarTypeService;

@WebMvcTest(DeleteRadarController.class)
public class DeleteRadarControllerTests extends AbstractControllerTests {
  @MockitoBean
  private RadarService radarService;

  @MockitoBean
  private RadarTypeService radarTypeService;

  /* TODO:
  @Test
  public void shouldGetRadars() throws Exception {
    //Create radarType
    final RadarTypeDto radarTypeDto = new RadarTypeDto();
    radarTypeDto.setId(10L);
    radarTypeDto.setDescription("My Description");
    radarTypeDto.setTitle("My title");
    radarTypeDto.setCode("My code");

    //Create radar for radarType
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(radarTypeDto.getId());
    radarDto.setRadarTypeTitle(radarTypeDto.getTitle());
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<RadarDto> radarDtoList = List.of(radarDto);
    Page<RadarDto> page = new PageImpl<>(radarDtoList);
    Mockito.when(radarService.findAll(any(), any())).thenReturn(page);

    MvcResult result = mockMvc.perform(get("/radars"))
        .andExpect(status().isOk())
        .andExpect(view().name("radars/index"))
        .andExpect(model().attributeExists("radarDtoPage"))
        .andExpect(model().attributeExists("pageNumbers"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains(radarDto.getTitle()));
    Assertions.assertTrue(content.contains(radarDto.getDescription()));

    Mockito.verify(radarService).findAll(any(), any());
  }

  @Test
  public void shouldShowRadar() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    Mockito.when(radarService.findById(any())).thenReturn(Optional.of(radarDto));

    String url = String.format("/radars/show/%d", radarDto.getId());
    MvcResult result = mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(view().name("radars/show"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains(radarDto.getTitle()));
    Assertions.assertTrue(content.contains(radarDto.getDescription()));

    Mockito.verify(radarService).findById(radarDto.getId());
  }

  @Test
  public void shouldRedirectShowRadar() throws Exception {
    Mockito.when(radarService.findById(any())).thenReturn(Optional.empty());

    MvcResult result = mockMvc.perform(get("/radars/show/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/radars"))
        .andExpect(MockMvcResultMatchers.flash().attribute(FlashMessages.ERROR, "Invalid radar id."))
        .andReturn();

    Mockito.verify(radarService).findById(any());
  }

   */
}
