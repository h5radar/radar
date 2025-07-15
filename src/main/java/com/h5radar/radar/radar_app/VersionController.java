package com.h5radar.radar.domain.radar_app;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Application version API")
@RequestMapping("/api/v1/radar-app/version")
@RequiredArgsConstructor
@EnableConfigurationProperties(Version.class)
public class VersionController {

  private final Version version;

  @GetMapping("")
  public Version show() {
    return version;
  }
}
