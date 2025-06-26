package com.h5radar.radar.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.h5radar.radar.RadarConstants;
import com.h5radar.radar.domain.radar_user.RadarUserDto;
import com.h5radar.radar.domain.radar_user.RadarUserService;

@Order()
public class AuthSecurityFilter extends GenericFilterBean {
  /*
   * Bearer constants from Authorization header
   */
  protected static final String BEARER = "Bearer ";

  private final RadarUserService radarUserService;

  public AuthSecurityFilter(RadarUserService radarUserService) {
    this.radarUserService = radarUserService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {

    RadarUserDto radarUserDto = new RadarUserDto();
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
      radarUserDto.setSub(rootNode.get("sub").asText());
      radarUserDto.setUsername(rootNode.get("preferred_username").asText());
    } else {
      // This code primary for tests
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication auth = securityContext.getAuthentication();
      radarUserDto.setSub(auth.getName());
      radarUserDto.setUsername(auth.getName());
    }

    // Try to find radar user by sub
    Optional<RadarUserDto> radarUserDtoOptional =  radarUserService.findBySub(radarUserDto.getSub());
    if (radarUserDtoOptional.isPresent()) {
      request.setAttribute(RadarConstants.RARDAR_USER_ID_ATTRIBUTE_NAME, radarUserDtoOptional.get().getId());
    } else {
      radarUserDto = radarUserService.save(radarUserDto);
      request.setAttribute(RadarConstants.RARDAR_USER_ID_ATTRIBUTE_NAME, radarUserDto.getId());
    }

    // Continue processing the request
    chain.doFilter(request, response);
  }
}
