package com.h5radar.radar.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h5radar.radar.RadarConstants;
import com.h5radar.radar.domain.radar_user.RadarUserDto;
import com.h5radar.radar.domain.radar_user.RadarUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Base64;
import java.util.Enumeration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthRequestInterceptor implements HandlerInterceptor {
  /*
   * Bearer constants from Authorization header
   */
  protected static final String BEARER = "Bearer ";
  /*
   * Radar user sub constraint name
   */
  private static final String RADAR_USERS_SUB_CONSTRAINTS = "uc_radar_users_sub";


  @Autowired
  private RadarUserService radarUserService;


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    RadarUserDto radarUserDto = new RadarUserDto();
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
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
      // This code primary for tests (context is set based on @WithMockUser)
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication auth = securityContext.getAuthentication();
      radarUserDto.setSub(auth.getName());
      radarUserDto.setUsername(auth.getName());
    }

    try {
      radarUserDto = radarUserService.save(radarUserDto);
      request.setAttribute(RadarConstants.RARDAR_USER_ID_ATTRIBUTE_NAME, radarUserDto.getId());
    } catch (DataIntegrityViolationException exception) {
      if (exception.getMessage().toLowerCase().contains(RADAR_USERS_SUB_CONSTRAINTS)) {
        Optional<RadarUserDto> radarUserDtoOptional =  radarUserService.findBySub(radarUserDto.getSub());
        if (radarUserDtoOptional.isPresent()) {
          request.setAttribute(RadarConstants.RARDAR_USER_ID_ATTRIBUTE_NAME, radarUserDtoOptional.get().getId());
          return true;
        }
      }
      throw exception;
    }

    // Continue processing the request
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response,
                         Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
    System.out.println("PostHandle: Processing request completed.");
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) throws Exception {
    System.out.println("AfterCompletion: Request processing completed.");
  }
}
