package com.h5radar.radar.domain.technology_blip;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractControllerTests;
import com.h5radar.radar.domain.radar.RadarService;
import com.h5radar.radar.domain.ring.RingService;
import com.h5radar.radar.domain.segment.SegmentService;
import com.h5radar.radar.domain.technology.TechnologyService;

@WebMvcTest(DeleteTechnologyBlipCfgController.class)
public class DeleteTechnologyBlipCfgControllerTests extends AbstractControllerTests {
  @MockitoBean
  private TechnologyBlipService technologyBlipService;
  @MockitoBean
  private RadarService radarService;
  @MockitoBean
  private TechnologyService technologyService;
  @MockitoBean
  private SegmentService segmentService;
  @MockitoBean
  private RingService ringService;

  /* TODO:
  @Test
  public void shouldGetTechnologyBlips() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(1L);
    radarDto.setTitle("My radar");
    radarDto.setDescription("My radar description");

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(2L);
    segmentDto.setRadarId(radarDto.getId());
    segmentDto.setTitle("My segment title");
    segmentDto.setDescription("My segment description");
    segmentDto.setPosition(1);

    final RingDto ringDto = new RingDto();
    ringDto.setId(3L);
    ringDto.setRadarId(radarDto.getId());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setPosition(1);
    ringDto.setColor("#fbdb84");

    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(4L);
    technologyDto.setTitle("My technology");
    technologyDto.setWebsite("My website");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    final TechnologyBlipDto technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(5L);
    technologyBlipDto.setRadarId(radarDto.getId());
    technologyBlipDto.setRadarTitle(radarDto.getTitle());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setRingTitle(ringDto.getTitle());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setTechnologyTitle(technologyDto.getTitle());
    technologyBlipDto.setSegmentId(segmentDto.getId());
    technologyBlipDto.setSegmentTitle(segmentDto.getTitle());

    Page<TechnologyBlipDto> page = new PageImpl<>(List.of(technologyBlipDto));
    Mockito.when(technologyBlipService.findAll(any(), any())).thenReturn(page);

    MvcResult result = mockMvc.perform(get("/settings/technology_blips"))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/technology_blips/index"))
        .andExpect(model().attributeExists("technologyBlipDtoPage"))
        .andExpect(model().attributeExists("pageNumbers"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains(technologyBlipDto.getRadarTitle()));
    Assertions.assertTrue(content.contains(technologyBlipDto.getTechnologyTitle()));
    Assertions.assertTrue(content.contains(technologyBlipDto.getSegmentTitle()));
    Assertions.assertTrue(content.contains(technologyBlipDto.getRingTitle()));

    Mockito.verify(technologyBlipService).findAll(any(), any());
  }

  @Test
  public void shouldShowTechnologyBlip() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(1L);
    radarDto.setTitle("My radar");
    radarDto.setDescription("My radar description");

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(2L);
    segmentDto.setRadarId(radarDto.getId());
    segmentDto.setTitle("My segment title");
    segmentDto.setDescription("My segment description");
    segmentDto.setPosition(1);

    final RingDto ringDto = new RingDto();
    ringDto.setId(3L);
    ringDto.setRadarId(radarDto.getId());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setPosition(1);
    ringDto.setColor("#fbdb84");

    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(4L);
    technologyDto.setTitle("My technology");
    technologyDto.setWebsite("My website");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    final TechnologyBlipDto technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(5L);
    technologyBlipDto.setRadarId(radarDto.getId());
    technologyBlipDto.setRadarTitle(radarDto.getTitle());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setRingTitle(ringDto.getTitle());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setTechnologyTitle(technologyDto.getTitle());
    technologyBlipDto.setSegmentId(segmentDto.getId());
    technologyBlipDto.setSegmentTitle(segmentDto.getTitle());

    Mockito.when(technologyBlipService.findById(any())).thenReturn(Optional.of(technologyBlipDto));

    String url = String.format("/settings/technology_blips/show/%d", technologyBlipDto.getId());
    MvcResult result = mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/technology_blips/show"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains(technologyBlipDto.getRadarTitle()));
    Assertions.assertTrue(content.contains(technologyBlipDto.getTechnologyTitle()));
    Assertions.assertTrue(content.contains(technologyBlipDto.getSegmentTitle()));
    Assertions.assertTrue(content.contains(technologyBlipDto.getRingTitle()));

    Mockito.verify(technologyBlipService).findById(technologyBlipDto.getId());
  }

  @Test
  public void shouldRedirectShowTechnologyBlip() throws Exception {
    Mockito.when(technologyBlipService.findById(any())).thenReturn(Optional.empty());

    MvcResult result = mockMvc.perform(get("/settings/technology_blips/show/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/technology_blips"))
        .andExpect(MockMvcResultMatchers.flash().attribute(FlashMessages.ERROR, "Invalid technology blip id."))
        .andReturn();

    Mockito.verify(technologyBlipService).findById(any());
  }

  @Test
  public void shouldAddTechnologyBlip() throws Exception {
    MvcResult result = mockMvc.perform(get("/settings/technology_blips/add"))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/technology_blips/add"))
        .andExpect(model().attributeExists("technologyBlipDto"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("radar"));
    Assertions.assertTrue(content.contains("Technology"));
    Assertions.assertTrue(content.contains("Segment"));
    Assertions.assertTrue(content.contains("ring"));
  }

  @Test
  public void shouldCreateTechnologyBlip() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(1L);
    radarDto.setTitle("My radar");
    radarDto.setDescription("My radar description");

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(2L);
    segmentDto.setRadarId(radarDto.getId());
    segmentDto.setTitle("My segment title");
    segmentDto.setDescription("My segment description");
    segmentDto.setPosition(1);

    final RingDto ringDto = new RingDto();
    ringDto.setId(3L);
    ringDto.setRadarId(radarDto.getId());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setPosition(1);
    ringDto.setColor("#fbdb84");

    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(4L);
    technologyDto.setTitle("My technology");
    technologyDto.setWebsite("My website");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    final TechnologyBlipDto technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(5L);
    technologyBlipDto.setRadarId(radarDto.getId());
    technologyBlipDto.setRadarTitle(radarDto.getTitle());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setRingTitle(ringDto.getTitle());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setTechnologyTitle(technologyDto.getTitle());
    technologyBlipDto.setSegmentId(segmentDto.getId());
    technologyBlipDto.setSegmentTitle(segmentDto.getTitle());

    Mockito.when(technologyBlipService.save(any(TechnologyBlipDto.class))).thenReturn(technologyBlipDto);

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarId", String.valueOf(technologyBlipDto.getRadarId()))
            .param("technologyId", String.valueOf(technologyBlipDto.getTechnologyId()))
            .param("segmentId", String.valueOf(technologyBlipDto.getSegmentId()))
            .param("ringId", String.valueOf(technologyBlipDto.getRingId()))
            .sessionAttr("technologyBlipDto", technologyBlipDto))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/technology_blips"))
        .andExpect(MockMvcResultMatchers.flash()
            .attribute(FlashMessages.INFO, "The technology blip has been created successfully."))
        .andReturn();

    Assertions.assertEquals(technologyBlipDto.getRadarId(), radarDto.getId());
    Assertions.assertEquals(technologyBlipDto.getRadarTitle(), radarDto.getTitle());
    Assertions.assertEquals(technologyBlipDto.getRingId(), ringDto.getId());
    Assertions.assertEquals(technologyBlipDto.getRingTitle(), ringDto.getTitle());
    Assertions.assertEquals(technologyBlipDto.getSegmentId(), segmentDto.getId());
    Assertions.assertEquals(technologyBlipDto.getSegmentTitle(), segmentDto.getTitle());
    Assertions.assertEquals(technologyBlipDto.getTechnologyId(), technologyDto.getId());
    Assertions.assertEquals(technologyBlipDto.getTechnologyTitle(), technologyDto.getTitle());

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldFailToCreateTechnologyBlipDueToNoData() throws Exception {
    List<ModelError> modelErrorList = List.of(new ModelError(null, "must not be null", "radar"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(technologyBlipService)
        .save(any(TechnologyBlipDto.class));

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/technology_blips/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("must not be null"));

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldFailToCreateTechnologyBlipDueToRadarAndTechnologyIsNotUnique() throws Exception {
    List<ModelError> modelErrorList =
        List.of(new ModelError(null, "radar and technology should be unique", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(technologyBlipService)
        .save(any(TechnologyBlipDto.class));

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("technologyBlipDto"))
        .andExpect(view().name("settings/technology_blips/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("radar and technology should be unique"));

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldFailToCreateTechnologyBlipDueToEmptyField() throws Exception {
    List<ModelError> modelErrorList =
        List.of(new ModelError(null, "should not be equal null", "radar"),
            new ModelError(null, "should not be equal null", "segment"),
            new ModelError(null, "should not be equal null", "ring"),
            new ModelError(null, "should not be equal null", "technology"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(technologyBlipService)
        .save(any(TechnologyBlipDto.class));

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "radarId"))
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "segmentId"))
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "ringId"))
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "technologyId"))
        .andExpect(view().name("settings/technology_blips/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should not be equal null"));

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldEditTechnologyBlip() throws Exception {
    final RadarTypeDto radarTypeDto = new RadarTypeDto();
    radarTypeDto.setId(1L);
    radarTypeDto.setTitle("Technology radars 1");
    radarTypeDto.setCode("technology_radar_1");
    radarTypeDto.setDescription("Technology radars");

    // Create a radar
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(2L);
    radarDto.setRadarTypeId(radarTypeDto.getId());
    radarDto.setRadarTypeTitle(radarTypeDto.getTitle());
    radarDto.setTitle("My new test Radar");
    radarDto.setDescription("My awesome description");
    radarDto.setPrimary(false);
    radarDto.setActive(false);

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(3L);
    segmentDto.setRadarId(radarDto.getId());
    segmentDto.setTitle("My segment title");
    segmentDto.setDescription("My segment description");
    segmentDto.setPosition(1);

    final RingDto ringDto = new RingDto();
    ringDto.setId(4L);
    ringDto.setRadarId(radarDto.getId());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setPosition(1);
    ringDto.setColor("#fbdb84");

    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(5L);
    technologyDto.setTitle("My technology");
    technologyDto.setWebsite("My website");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    final TechnologyBlipDto technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(6L);
    technologyBlipDto.setRadarId(radarDto.getId());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setSegmentId(segmentDto.getId());

    Mockito.when(technologyBlipService.findById(any())).thenReturn(Optional.of(technologyBlipDto));

    String url = String.format("/settings/technology_blips/edit/%d", technologyBlipDto.getId());
    MvcResult result = mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/technology_blips/edit"))
        .andReturn();

    Mockito.verify(technologyBlipService).findById(technologyBlipDto.getId());
  }

  @Test
  public void shouldRedirectEditTechnologyBlip() throws Exception {
    Mockito.when(technologyBlipService.findById(any())).thenReturn(Optional.empty());

    MvcResult result = mockMvc.perform(get("/settings/technology_blips/edit/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/technology_blips"))
        .andExpect(MockMvcResultMatchers.flash().attribute(FlashMessages.ERROR, "Invalid technology blip id."))
        .andReturn();

    Mockito.verify(technologyBlipService).findById(any());
  }

  @Test
  public void shouldUpdateTechnologyBlip() throws Exception {
    final RadarTypeDto radarTypeDto = new RadarTypeDto();
    radarTypeDto.setId(1L);
    radarTypeDto.setTitle("Technology radars 1");
    radarTypeDto.setCode("technology_radar_1");
    radarTypeDto.setDescription("Technology radars");

    // Create a radar
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(2L);
    radarDto.setRadarTypeId(radarTypeDto.getId());
    radarDto.setRadarTypeTitle(radarTypeDto.getTitle());
    radarDto.setTitle("My new test Radar");
    radarDto.setDescription("My awesome description");
    radarDto.setPrimary(false);
    radarDto.setActive(false);

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(2L);
    segmentDto.setRadarId(radarDto.getId());
    segmentDto.setTitle("My segment title");
    segmentDto.setDescription("My segment description");
    segmentDto.setPosition(1);

    final RingDto ringDto = new RingDto();
    ringDto.setId(3L);
    ringDto.setRadarId(radarDto.getId());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setPosition(1);
    ringDto.setColor("#fbdb84");

    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(4L);
    technologyDto.setTitle("My technology");
    technologyDto.setWebsite("My website");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    final TechnologyBlipDto technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(5L);
    technologyBlipDto.setRadarId(radarDto.getId());
    technologyBlipDto.setRadarTitle(radarDto.getTitle());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setRingTitle(ringDto.getTitle());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setTechnologyTitle(technologyDto.getTitle());
    technologyBlipDto.setSegmentId(segmentDto.getId());
    technologyBlipDto.setSegmentTitle(segmentDto.getTitle());

    Mockito.when(technologyBlipService.save(any(TechnologyBlipDto.class))).thenReturn(technologyBlipDto);

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarId", String.valueOf(technologyBlipDto.getRadarId()))
            .param("technologyId", String.valueOf(technologyBlipDto.getTechnologyId()))
            .param("segmentId", String.valueOf(technologyBlipDto.getSegmentId()))
            .param("ringId", String.valueOf(technologyBlipDto.getRingId()))
            .sessionAttr("technologyBlipDto", technologyBlipDto))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/technology_blips"))
        .andExpect(MockMvcResultMatchers.flash()
            .attribute(FlashMessages.INFO, "The technology blip has been updated successfully."))
        .andReturn();

    Assertions.assertEquals(technologyBlipDto.getRadarId(), radarDto.getId());
    Assertions.assertEquals(technologyBlipDto.getRadarTitle(), radarDto.getTitle());
    Assertions.assertEquals(technologyBlipDto.getRingId(), ringDto.getId());
    Assertions.assertEquals(technologyBlipDto.getRingTitle(), ringDto.getTitle());
    Assertions.assertEquals(technologyBlipDto.getSegmentId(), segmentDto.getId());
    Assertions.assertEquals(technologyBlipDto.getSegmentTitle(), segmentDto.getTitle());
    Assertions.assertEquals(technologyBlipDto.getTechnologyId(), technologyDto.getId());
    Assertions.assertEquals(technologyBlipDto.getTechnologyTitle(), technologyDto.getTitle());

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldFailToUpdateTechnologyBlipDueToNoData() throws Exception {
    List<ModelError> modelErrorList = List.of(new ModelError(null, "must not be null", "radar"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(technologyBlipService)
        .save(any(TechnologyBlipDto.class));

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/technology_blips/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("must not be null"));

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldFailToUpdateTechnologyBlipDueToRadarAndTechnologyIsNotUnique() throws Exception {
    List<ModelError> modelErrorList =
        List.of(new ModelError(null, "radar and technology should be unique", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(technologyBlipService)
        .save(any(TechnologyBlipDto.class));

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("technologyBlipDto"))
        .andExpect(view().name("settings/technology_blips/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("radar and technology should be unique"));

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldFailToUpdateTechnologyBlipDueToEmptyField() throws Exception {
    List<ModelError> modelErrorList =
        List.of(new ModelError(null, "should not be equal null", "radar"),
            new ModelError(null, "should not be equal null", "segment"),
            new ModelError(null, "should not be equal null", "ring"),
            new ModelError(null, "should not be equal null", "technology"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(technologyBlipService)
        .save(any(TechnologyBlipDto.class));

    MvcResult result = mockMvc.perform(post("/settings/technology_blips/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "radarId"))
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "segmentId"))
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "ringId"))
        .andExpect(model().attributeHasFieldErrors("technologyBlipDto", "technologyId"))
        .andExpect(view().name("settings/technology_blips/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should not be equal null"));

    Mockito.verify(technologyBlipService).save(any(TechnologyBlipDto.class));
  }

  @Test
  public void shouldDeleteTechnologyBlip() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(1L);
    radarDto.setTitle("My radar");
    radarDto.setDescription("My radar description");

    final SegmentDto segmentDto = new SegmentDto();
    segmentDto.setId(2L);
    segmentDto.setRadarId(radarDto.getId());
    segmentDto.setTitle("My segment title");
    segmentDto.setDescription("My segment description");
    segmentDto.setPosition(1);

    final RingDto ringDto = new RingDto();
    ringDto.setId(3L);
    ringDto.setRadarId(radarDto.getId());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setPosition(1);
    ringDto.setColor("#fbdb84");

    final TechnologyDto technologyDto = new TechnologyDto();
    technologyDto.setId(4L);
    technologyDto.setTitle("My technology");
    technologyDto.setWebsite("My website");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    final TechnologyBlipDto technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(5L);
    technologyBlipDto.setRadarId(radarDto.getId());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setSegmentId(segmentDto.getId());

    Mockito.doAnswer((i) -> null).when(technologyBlipService).deleteById(any());

    String url = String.format("/settings/technology_blips/delete/%d", technologyBlipDto.getId());
    MvcResult result = mockMvc.perform(get(url))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/technology_blips"))
        .andExpect(MockMvcResultMatchers.flash()
            .attribute(FlashMessages.INFO, "The technology blip has been deleted successfully."))
        .andReturn();

    Mockito.verify(technologyBlipService).deleteById(technologyBlipDto.getId());
  }

   */
}
