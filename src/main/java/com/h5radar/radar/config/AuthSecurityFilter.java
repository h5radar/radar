package com.h5radar.radar.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

@Order(Ordered.LOWEST_PRECEDENCE)
public class AuthSecurityFilter extends GenericFilterBean {
  /*
   * Bearer constants from Authorization header
   */
  protected static final String BEARER = "Bearer ";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.startsWith(BEARER)) {
      String token = authHeader.substring(BEARER.length());
      String[] chunks = token.split("\\.");
      Base64.Decoder decoder = Base64.getUrlDecoder();
      String header = new String(decoder.decode(chunks[0]));
      String payload = new String(decoder.decode(chunks[1]));
      System.out.println(header);
      System.out.println(payload);
    }

    // Continue processing the request
    chain.doFilter(request, response);
  }
}
