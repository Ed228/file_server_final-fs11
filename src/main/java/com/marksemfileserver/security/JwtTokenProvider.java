package com.marksemfileserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.header}")
  private String authorizationHeader;

  public boolean validateToken(String token) {
    try {
      String secretKey = TextCodec.BASE64URL.encode(secret);
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public String resolveToken(HttpServletRequest request) {
    String token = null;
    if(this.resolveTokenFromHeader(request) != null) {
      token = this.resolveTokenFromHeader((request));
    } else if (this.resolveTokenFormQueryParam(request) != null) {
      token = this.resolveTokenFormQueryParam(request);
    }

    return token;
  }

  private String resolveTokenFromHeader(HttpServletRequest request) {
    return request.getHeader(authorizationHeader);
  }

  private String resolveTokenFormQueryParam(HttpServletRequest request) {
    return request.getParameter("jwt");
  }

}
