package com.h5radar.radar.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h5radar.radar.domain.radar_user.RadarUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.h5radar.radar.config.ApplicationTestBaseConfig;
import com.h5radar.radar.config.SecurityConfiguration;

@ApplicationTestBaseConfig
@Import(SecurityConfiguration.class)
public abstract class AbstractControllerTests extends AbstractAnyTests {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockitoBean
  protected RadarUserService radarUserService;
}
