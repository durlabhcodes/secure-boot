package com.durlabh.codes.secure_boot.filter;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

import static com.durlabh.codes.secure_boot.Constants.*;

//@Component
public class JWTGenerationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            String jwt = Jwts.builder()
                    .issuer(ISSUER)
                    .claim("email", authentication.getName())
                    .claim("authorities", authentication.getAuthorities()
                            .stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .subject(authentication.getName())
                    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .compact();
            response.setHeader(JWT_HEADER, jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/api/login") &&
                !request.getServletPath().startsWith("/login");
    }
}
