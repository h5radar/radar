package com.h5radar.radar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.h5radar.radar.config.ApplicationTestBaseConfig;
import com.h5radar.radar.config.SecurityConfiguration;
import com.h5radar.radar.radar_user.RadarUserService;

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
