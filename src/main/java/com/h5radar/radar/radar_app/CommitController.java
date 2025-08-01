package com.h5radar.radar.radar_app;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Application commit API")
@RequestMapping("/api/v1/radar-app/commit")
@RequiredArgsConstructor
@EnableConfigurationProperties(Commit.class)
public class CommitController {
  private final Commit commit;

  @GetMapping("")
  public Commit show() {
    return commit;
  }
}
