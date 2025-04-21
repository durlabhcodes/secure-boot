package com.durlabh.codes.secure_boot.controller;

import com.durlabh.codes.secure_boot.model.LoginRequestDTO;
import com.durlabh.codes.secure_boot.model.LoginResponseDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static com.durlabh.codes.secure_boot.Constants.JWT_HEADER;
import static com.durlabh.codes.secure_boot.Constants.SECRET_KEY;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ApiController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> apiLogin (@RequestBody LoginRequestDTO loginRequest) {
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),
                loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            jwt = Jwts.builder().issuer("durlabhcodes").subject("JWT Token")
                    .claim("username", authenticationResponse.getName())
                    .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                    .issuedAt(new java.util.Date())
                    .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                    .signWith(secretKey).compact();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header(JWT_HEADER, jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }
}
