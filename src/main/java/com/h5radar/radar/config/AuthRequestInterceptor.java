package com.h5radar.radar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthRequestInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // Enumeration<String> headerNames = request.getHeaderNames();
    Enumeration<String> headerValues = request.getHeaders("Authorization");
    System.out.println("PreHandle: Intercepting request...");
    System.out.println("Request URI: " + request.getRequestURI());
    System.out.println("Request Auth: " + headerValues.toString());

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
