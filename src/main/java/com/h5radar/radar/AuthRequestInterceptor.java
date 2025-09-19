package com.h5radar.radar;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.h5radar.radar.radar_user.RadarUserDto;
import com.h5radar.radar.radar_user.RadarUserService;


@Component
public class AuthRequestInterceptor implements HandlerInterceptor {
  /*
   * Radar user sub constraint name
   */
  private static final String RADAR_USERS_SUB_CONSTRAINTS = "uc_radar_users_sub";

  @Autowired
  private RadarUserService radarUserService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    RadarUserDto radarUserDto = new RadarUserDto();

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
      throw new AccessDeniedException("JWT authentication required");
    }

    Jwt jwt = jwtAuth.getToken();
    radarUserDto.setSub(jwt.getClaimAsString("sub"));
    radarUserDto.setUsername(jwt.getClaimAsString("preferred_username"));

    try {
      Optional<RadarUserDto> radarUserDtoOptional = radarUserService.findBySub(radarUserDto.getSub());
      if (radarUserDtoOptional.isPresent()) {
        request.setAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME, radarUserDtoOptional.get().getId());
      } else {
        radarUserDto = radarUserService.save(radarUserDto);
        Long radarUserId = radarUserDto == null ? 0 : radarUserDto.getId();
        request.setAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME, radarUserId);
      }
    } catch (DataIntegrityViolationException exception) {
      // Handle race condition
      if (exception.getMessage().toLowerCase().contains(RADAR_USERS_SUB_CONSTRAINTS)) {
        Optional<RadarUserDto> radarUserDtoOptional =  radarUserService.findBySub(radarUserDto.getSub());
        if (radarUserDtoOptional.isPresent()) {
          request.setAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME, radarUserDtoOptional.get().getId());
          return true;
        }
      }
      throw exception;
    }
    return true;
  }
}
