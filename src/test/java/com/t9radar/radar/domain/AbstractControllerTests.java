package com.t9radar.radar.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.t9radar.radar.config.ApplicationTestBaseConfig;
import com.t9radar.radar.config.SecurityConfiguration;

@ApplicationTestBaseConfig
@Import(SecurityConfiguration.class)
public abstract class AbstractControllerTests extends AbstractAnyTests {
  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;
}