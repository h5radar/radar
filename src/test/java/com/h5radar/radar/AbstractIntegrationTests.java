package com.h5radar.radar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.h5radar.radar.radar_user.RadarUserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RadarApplication.class)
public abstract class AbstractIntegrationTests extends AbstractAnyTests {

  @Autowired
  protected WebTestClient webTestClient;

  @Autowired
  protected RadarUserService radarUserService;

  @Autowired
  public void setWebApplicationContext(final WebApplicationContext context) {
    webTestClient = MockMvcWebTestClient.bindToApplicationContext(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .defaultRequest(
            MockMvcRequestBuilders.get("/")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> {
                  j.claim("sub", "My sub");
                  j.claim("preferred_username", "My username");
                }))
        )
        .configureClient()
        .build();
  }
}
