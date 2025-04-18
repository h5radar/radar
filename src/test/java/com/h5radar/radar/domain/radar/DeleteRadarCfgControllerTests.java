package com.h5radar.radar.domain.radar;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractControllerTests;
import com.h5radar.radar.domain.radar_type.RadarTypeService;

@WebMvcTest(DeleteRadarCfgController.class)
public class DeleteRadarCfgControllerTests extends AbstractControllerTests {
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
    radarTypeDto.setCode(RadarType.TECHNOLOGY_RADAR);

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

    MvcResult result = mockMvc.perform(get("/settings/radars"))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/radars/index"))
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

    String url = String.format("/settings/radars/show/%d", radarDto.getId());
    MvcResult result = mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/radars/show"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains(radarDto.getTitle()));
    Assertions.assertTrue(content.contains(radarDto.getDescription()));

    Mockito.verify(radarService).findById(radarDto.getId());
  }

  @Test
  public void shouldRedirectShowRadar() throws Exception {
    Mockito.when(radarService.findById(any())).thenReturn(Optional.empty());

    MvcResult result = mockMvc.perform(get("/settings/radars/show/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/radars"))
        .andExpect(MockMvcResultMatchers.flash().attribute(FlashMessages.ERROR, "Invalid radar id."))
        .andReturn();

    Mockito.verify(radarService).findById(any());
  }

  @Test
  public void shouldAddRadar() throws Exception {
    MvcResult result = mockMvc.perform(get("/settings/radars/add"))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/radars/add"))
        .andExpect(model().attributeExists("radarDto"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("title"));
    Assertions.assertTrue(content.contains("description"));
  }

  @Test
  public void shouldCreateRadar() throws Exception {
    final RadarTypeDto radarTypeDto = new RadarTypeDto();
    radarTypeDto.setId(10L);
    radarTypeDto.setDescription("My Description");
    radarTypeDto.setTitle("My title");
    radarTypeDto.setCode(RadarType.TECHNOLOGY_RADAR);

    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(radarTypeDto.getId());
    radarDto.setRadarTypeTitle(radarTypeDto.getTitle());
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    Mockito.when(radarService.save(any(RadarDto.class))).thenReturn(radarDto);

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/radars"))
        .andExpect(
            MockMvcResultMatchers.flash().attribute(FlashMessages.INFO, "The radar has been created successfully."))
        .andReturn();

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToCreateRadarDueToTitleWithWhiteSpace() throws Exception {
    List<ModelError> modelErrorList = List.of(
        new ModelError(null, "should be without whitespaces before and after", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService)
        .save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", " My title "))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should be without whitespaces before and after"));

    Mockito.verify(radarService).save(any());

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToCreateRadarDueToUnableToCreate() throws Exception {
    final RadarTypeDto radarTypeDto = new RadarTypeDto();
    radarTypeDto.setId(1L);
    radarTypeDto.setDescription("My Description");
    radarTypeDto.setTitle("My title");
    radarTypeDto.setCode(RadarType.TECHNOLOGY_RADAR);

    final RadarDto radarDto = new RadarDto();
    radarDto.setId(2L);
    radarDto.setRadarTypeId(radarTypeDto.getId());
    radarDto.setRadarTypeTitle(radarTypeDto.getTitle());
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList =
        List.of(new ModelError("radar.error.exception", "Unable to create radar due to error: {0}", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("radarDto"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("Unable to create radar due to error: {0}"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  void shouldFailToCreateActiveRadarDtoDueToMinimumRingAndSegment() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "active radar should have four rings", null),
        new ModelError(null, "active radar should have four segments", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("radarDto"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("active radar should have four rings"));
    Assertions.assertTrue(content.contains("active radar should have four segments"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  void shouldFailToCreateActiveRadarDtoDueToWrongRingAndSegmentPosition() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList =
        List.of(new ModelError(null, "active radar rings should be consecutively numbered from 0 till 3", null),
            new ModelError(null, "active radar segments should be consecutively numbered from 0 till 3", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("radarDto"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("active radar rings should be consecutively numbered from 0 till 3"));
    Assertions.assertTrue(content.contains("active radar segments should be consecutively numbered from 0 till 3"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToCreateRadarDueToEmptyTitle() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "must not be blank", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("must not be blank"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToCreateRadarDueToNotUniqueTitle() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "is already taken", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("is already taken"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToCreateSecondPrimaryRadarDtoAndEmptyTitle() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "must not be blank", "title"),
        new ModelError(null, "should be only one primary radar", "primary"),
        new ModelError(null, "size must be between 1 and 64", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(model().attributeHasFieldErrors("radarDto", "primary"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should be only one primary radar"));
    Assertions.assertTrue(content.contains("must not be blank"));
    Assertions.assertTrue(content.contains("size must be between 1 and 64"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToCreateRadarDueToNotOnePrimary() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "should be only one primary radar", "primary"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "primary"))
        .andExpect(view().name("settings/radars/add"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should be only one primary radar"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldEditRadar() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);
    Mockito.when(radarService.findById(any())).thenReturn(Optional.of(radarDto));

    String url = String.format("/settings/radars/edit/%d", radarDto.getId());
    MvcResult result = mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains(radarDto.getTitle()));
    Assertions.assertTrue(content.contains(radarDto.getDescription()));

    Mockito.verify(radarService).findById(radarDto.getId());
  }

  @Test
  public void shouldRedirectEditRadar() throws Exception {
    Mockito.when(radarService.findById(any())).thenReturn(Optional.empty());

    MvcResult result = mockMvc.perform(get("/settings/radars/edit/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/radars"))
        .andExpect(MockMvcResultMatchers.flash().attribute(FlashMessages.ERROR, "Invalid radar id."))
        .andReturn();

    Mockito.verify(radarService).findById(any());
  }

  @Test
  public void shouldUpdateRadar() throws Exception {
    final RadarTypeDto radarTypeDto = new RadarTypeDto();
    radarTypeDto.setId(10L);
    radarTypeDto.setDescription("My Description");
    radarTypeDto.setTitle("My title");
    radarTypeDto.setCode(RadarType.TECHNOLOGY_RADAR);

    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(radarTypeDto.getId());
    radarDto.setRadarTypeTitle(radarTypeDto.getTitle());
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    Mockito.when(radarService.save(any(RadarDto.class))).thenReturn(radarDto);

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/radars"))
        .andExpect(
            MockMvcResultMatchers.flash().attribute(FlashMessages.INFO, "The radar has been updated successfully."))
        .andReturn();

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToUpdateRadarDueToEmptyTitle() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type title");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "must not be blank", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("must not be blank"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToUpdateRadarDueToUnableToCreate() throws Exception {
    final RadarTypeDto radarTypeDto = new RadarTypeDto();
    radarTypeDto.setId(1L);
    radarTypeDto.setDescription("My Description");
    radarTypeDto.setTitle("My title");
    radarTypeDto.setCode(RadarType.TECHNOLOGY_RADAR);

    final RadarDto radarDto = new RadarDto();
    radarDto.setId(2L);
    radarDto.setRadarTypeId(radarTypeDto.getId());
    radarDto.setRadarTypeTitle(radarTypeDto.getTitle());
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "Unable to create radar due to error: {0}", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("radarDto"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("Unable to create radar due to error: {0}"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  void shouldFailToUpdateActiveRadarDtoDueToMinimumRingAndSegment() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "active radar should have four rings", null),
        new ModelError(null, "active radar should have four segments", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("radarDto"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("active radar should have four rings"));
    Assertions.assertTrue(content.contains("active radar should have four segments"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  void shouldFailToUpdateActiveRadarDtoDueToWrongRingAndSegmentPosition() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList =
        List.of(new ModelError(null, "active radar rings should be consecutively numbered from 0 till 3", null),
            new ModelError(null, "active radar segments should be consecutively numbered from 0 till 3", null));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("radarDto"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("active radar rings should be consecutively numbered from 0 till 3"));
    Assertions.assertTrue(content.contains("active radar segments should be consecutively numbered from 0 till 3"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToUpdateRadarDueToNotUniqueTitle() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "is already taken", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("is already taken"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToUpdateSecondPrimaryRadarDtoAndEmptyTitle() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "must not be blank", "title"),
        new ModelError(null, "should be only one primary radar", "primary"),
        new ModelError(null, "size must be between 1 and 64", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(model().attributeHasFieldErrors("radarDto", "primary"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should be only one primary radar"));
    Assertions.assertTrue(content.contains("must not be blank"));
    Assertions.assertTrue(content.contains("size must be between 1 and 64"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToUpdateRadarDueToNotOnePrimary() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    List<ModelError> modelErrorList = List.of(new ModelError(null, "should be only one primary radar", "primary"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService).save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("radarTypeId", String.valueOf(radarDto.getRadarTypeId()))
            .param("title", radarDto.getTitle())
            .param("description", radarDto.getDescription())
            .sessionAttr("radarDto", radarDto))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "primary"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should be only one primary radar"));

    Mockito.verify(radarService).save(any(RadarDto.class));
  }

  @Test
  public void shouldFailToUpdateRadarDueToTitleWithWhiteSpace() throws Exception {
    List<ModelError> modelErrorList = List.of(
        new ModelError(null, "should be without whitespaces before and after", "title"));
    String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
    Mockito.doThrow(new ValidationException(errorMessage, modelErrorList)).when(radarService)
        .save(any(RadarDto.class));

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/settings/radars/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", " My title "))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("radarDto", "title"))
        .andExpect(view().name("settings/radars/edit"))
        .andReturn();

    String content = result.getResponse().getContentAsString();
    Assertions.assertTrue(content.contains("should be without whitespaces before and after"));

    Mockito.verify(radarService).save(any());
  }

  @Test
  public void shouldDeleteRadar() throws Exception {
    final RadarDto radarDto = new RadarDto();
    radarDto.setId(10L);
    radarDto.setRadarTypeId(3L);
    radarDto.setRadarTypeTitle("My radar type title");
    radarDto.setTitle("My title");
    radarDto.setDescription("My description");
    radarDto.setPrimary(true);
    radarDto.setActive(true);

    Mockito.doAnswer((i) -> null).when(radarService).deleteById(any());

    String url = String.format("/settings/radars/delete/%d", radarDto.getId());
    MvcResult result = mockMvc.perform(get(url))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/settings/radars"))
        .andExpect(
            MockMvcResultMatchers.flash().attribute(FlashMessages.INFO, "The radar has been deleted successfully."))
        .andReturn();

    Mockito.verify(radarService).deleteById(radarDto.getId());
  }
  */
}
