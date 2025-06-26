package com.h5radar.radar.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.h5radar.radar.RadarConstants;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;

@Component
@Order()
public class AuthSecurityFilter extends GenericFilterBean {
  /*
   * Bearer constants from Authorization header
   */
  protected static final String BEARER = "Bearer ";

  private final RadarUserRepository radarUserRepository;

  @Autowired
  public AuthSecurityFilter(RadarUserRepository radarUserRepository) {
    this.radarUserRepository = radarUserRepository;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.startsWith(BEARER)) {
      // Get payload section from jwt token
      String token = authHeader.substring(BEARER.length());
      String[] chunks = token.split("\\.");
      Base64.Decoder decoder = Base64.getUrlDecoder();
      String payload = new String(decoder.decode(chunks[1]));

      // Save radarUser and set id as attribute
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(payload);
      String sub = rootNode.get("sub").asText();
      String username = rootNode.get("preferred_username").asText();
      RadarUser radarUser = new RadarUser(null, sub, username);
      radarUser = radarUserRepository.saveAndFlush(radarUser);
      request.setAttribute(RadarConstants.RARDAR_USER_ID_ATTRIBUTE_NAME, radarUser.getId());
    }

    // Continue processing the request
    chain.doFilter(request, response);
  }
}
