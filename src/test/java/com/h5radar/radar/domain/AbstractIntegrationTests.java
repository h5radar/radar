package com.h5radar.radar.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.h5radar.radar.RadarApplication;
import com.h5radar.radar.domain.radar_user.RadarUserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RadarApplication.class)
public abstract class AbstractIntegrationTests extends AbstractAnyTests {

  // @Autowired
  protected WebTestClient webTestClient;

  @MockitoBean
  protected RadarUserService radarUserService;

  @Autowired
  public void setWebApplicationContext(final WebApplicationContext context) {
    webTestClient = MockMvcWebTestClient.bindToApplicationContext(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .defaultRequest(MockMvcRequestBuilders.get("/").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .configureClient()
        .build();
  }
}
