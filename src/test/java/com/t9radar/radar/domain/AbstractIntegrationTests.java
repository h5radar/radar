package com.t9radar.radar.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.t9radar.radar.RadarApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RadarApplication.class)
public abstract class AbstractIntegrationTests extends AbstractAnyTests {

  // @Autowired
  protected WebTestClient webTestClient;

  @Autowired
  public void setWebApplicationContext(final WebApplicationContext context) {
    webTestClient = MockMvcWebTestClient.bindToApplicationContext(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .defaultRequest(MockMvcRequestBuilders.get("/").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .configureClient()
        .build();
  }
}
