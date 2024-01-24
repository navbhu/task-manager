package com.example.tasks.security;

import com.example.tasks.model.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {


    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String SECRET_KEY = "mysecretkey";
    private static final int ACCESS_TOKEN_VALIDITY = 2;

    private final JwtParser jwtParser;



    public JwtUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        Instant tokenCreateTime = Instant.now();
        Instant tokenValidity = tokenCreateTime.plus(Duration.ofDays(ACCESS_TOKEN_VALIDITY));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(tokenValidity))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        }
        catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(Date.from(Instant.now()));
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}
