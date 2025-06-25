package com.h5radar.radar.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

@Component
public class CustomRequestInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    System.out.println("PreHandle: Intercepting request...");
    System.out.println("Request URI: " + request.getRequestURI());
    return true; // Continue processing the request
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
    System.out.println("PostHandle: Processing request completed.");
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    System.out.println("AfterCompletion: Request processing completed.");
  }
}
