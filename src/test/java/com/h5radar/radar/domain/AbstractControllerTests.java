package com.h5radar.radar.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.h5radar.radar.config.ApplicationTestBaseConfig;
import com.h5radar.radar.config.SecurityConfiguration;

@ApplicationTestBaseConfig
@Import(SecurityConfiguration.class)
public abstract class AbstractControllerTests extends AbstractAnyTests {
  /*
   * Auth header with sub equal to alice
   */
  protected static final String AUTHORIZATION_HEADER = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6IC"
      + "JUT1Z3ekNsQVdIM2hzVDAtV1Jhemx4Z1k4RXhBd0NPb2hNTEg4dXEtVkM4In0.eyJleHAiOjE3NTA5Mjk5MzcsImlhdCI6MTc1MDkyOTYz"
      + "NywiYXV0aF90aW1lIjoxNzUwOTI5NjM3LCJqdGkiOiJiZTA1MWQ4OS05ZDc2LTQxYjQtOWU2Ni02MDIzZWVkNzMxNzYiLCJpc3MiOiJodH"
      + "RwOi8vbG9jYWxob3N0OjgxODAvcmVhbG1zL2g1cmFkYXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNTQ4M2NiZTgtMTVjZC00YmY1LTkx"
      + "MWUtNmNhNDU1ZTUzM2M5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBwLXVpIiwic2lkIjoiZDc1OGQ0NzUtOTJiOC00MDgyLWE2MzItZm"
      + "VhZjAwZTAyZTEyIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL2FwcC5oNXJhZGFyLnJ1IiwiaHR0cDovL2xvY2Fs"
      + "aG9zdDo1MTczIiwiaHR0cHM6Ly9hcHAuaDVyYWRhci5jb20iXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImVudGVycHJpc2UiXX0sIn"
      + "Jlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiXX19"
      + "LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiQWxpY2UgTGlkZGVsIiwicH"
      + "JlZmVycmVkX3VzZXJuYW1lIjoiYWxpY2UiLCJnaXZlbl9uYW1lIjoiQWxpY2UiLCJmYW1pbHlfbmFtZSI6IkxpZGRlbCIsImVtYWlsIjoi"
      + "YWxpY2VAZXhhbXBsZS5jb20ifQ.o_meoD5xqCYpsEEvVIT3DiQFWdhK8MxESGEXqt6_L_XpIbLqbywZozNI0OV9OcOP5vIHFAtJ6KlP0Gz"
      + "VMwrJOeVLlxS6vp_l6q8FEmbPM2Gp9usWFXc9STgkxCLkXMvmi0WTSGbiumfaPZxpcUmQTJf4Byy12otqExgNSmSke-yXV-Cx1k6aG7MNh"
      + "vPqqjOJ0HZxsNrkPVaI5P_TPMdQfiUk6GjlV_3FjBfLCZ4sKlFO7Qb9u9QvmkmfylF0ooX_sQiM_QaJ45agLQ9nQ9eJ6uQYA3XtNvtv2bJ"
      + "v16merTaqeZw-fXhJV5m6q8Lg2oA6jvhZMW9Fin6dqbPIFI5vbw";

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;
}
