package com.durlabh.codes.secure_boot.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

import static com.durlabh.codes.secure_boot.Constants.JWT_HEADER;
import static com.durlabh.codes.secure_boot.Constants.SECRET_KEY;

//@Component
public class JWTValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JWT_HEADER);
        if(jwt != null && !jwt.isEmpty()) {
            if(jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            SecretKey secret = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(secret)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                String email = claims.getSubject();
                String authorities = claims.get("authorities", String.class);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(token);
            } catch (Exception e) {
                // Handle invalid JWT token
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/login");
    }
}
