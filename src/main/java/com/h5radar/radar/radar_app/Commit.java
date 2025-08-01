package com.h5radar.radar.radar_app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties
public class Commit {

  @Value("${git.branch}")
  private String branch;

  @Value("${git.build.time}")
  private String buildTime;

  @Value("${git.build.version}")
  private String buildVersion;

  @Value("${git.commit.id.abbrev}")
  private String commitIdAbbrev;

  @Value("${git.commit.id.full}")
  private String commitIdFull;

  @Value("${git.commit.message.full}")
  private String commitMessageFull;

  @Value("${git.commit.message.short}")
  private String commitMessageShort;

  @Value("${git.commit.time}")
  private String commitTime;

  @Value("${git.dirty}")
  private String dirty;

  @Value("${git.tags}")
  private String tags;
}
