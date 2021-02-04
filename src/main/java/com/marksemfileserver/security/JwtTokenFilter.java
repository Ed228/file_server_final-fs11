package com.marksemfileserver.security;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

    try {
      if (!(token != null && jwtTokenProvider.validateToken(token))) {
        throw new JwtException("JWT token is expired or invalid");
      }
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (JwtException e) {
      ((HttpServletResponse) servletResponse).sendError(HttpStatus.UNAUTHORIZED.value(), "JWT token is expired or invalid");
    }
  }
}
