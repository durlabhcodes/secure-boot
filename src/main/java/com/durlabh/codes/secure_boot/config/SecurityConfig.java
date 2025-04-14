package com.durlabh.codes.secure_boot.config;

import com.durlabh.codes.secure_boot.filter.JWTGenerationFilter;
import com.durlabh.codes.secure_boot.filter.JWTValidationFilter;
import com.durlabh.codes.secure_boot.security.UsernamePwdAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Check Why its not working
        http.
                cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowCredentials(true);
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:9999"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .addFilterAfter(new JWTGenerationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTValidationFilter(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/login", "/apiLogin"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                    request
                            .requestMatchers("/hello", "/login", "/apiLogin")
                            .permitAll()
                            .anyRequest()
                            .authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        UsernamePwdAuthProvider provider = new UsernamePwdAuthProvider(userDetailsService, passwordEncoder);
        ProviderManager manager = new ProviderManager(provider);
        manager.setEraseCredentialsAfterAuthentication(false);
        return manager;
    }
}
