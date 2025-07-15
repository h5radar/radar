package com.h5radar.radar.domain.radar_app;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

import com.h5radar.radar.domain.AbstractControllerTests;

@WebMvcTest(VersionController.class)
public class VersionControllerTests extends AbstractControllerTests {
  @Test
  @WithAnonymousUser
  public void shouldShowVersion() throws Exception {
    mockMvc.perform(get("/api/v1/radar-app/version").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$").value(hasKey("groupId")))
        .andExpect(jsonPath("$").value(hasKey("artifactId")))
        .andExpect(jsonPath("$").value(hasKey("version")));
  }
}
