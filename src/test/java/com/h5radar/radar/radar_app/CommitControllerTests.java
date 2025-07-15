package com.h5radar.radar.radar_app;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

import com.h5radar.radar.AbstractControllerTests;

@WebMvcTest(CommitController.class)
public class CommitControllerTests extends AbstractControllerTests {
  @Test
  @WithAnonymousUser
  public void shouldShowCommit() throws Exception {
    mockMvc.perform(get("/api/v1/radar-app/commit").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$").value(hasKey("branch")))
        .andExpect(jsonPath("$").value(hasKey("buildTime")))
        .andExpect(jsonPath("$").value(hasKey("buildVersion")))
        .andExpect(jsonPath("$").value(hasKey("commitIdAbbrev")))
        .andExpect(jsonPath("$").value(hasKey("commitIdFull")))
        .andExpect(jsonPath("$").value(hasKey("commitMessageFull")))
        .andExpect(jsonPath("$").value(hasKey("commitMessageShort")))
        .andExpect(jsonPath("$").value(hasKey("commitTime")))
        .andExpect(jsonPath("$").value(hasKey("dirty")))
        .andExpect(jsonPath("$").value(hasKey("tags")));
  }
}
