package com.example.ears.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider,
                                 JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
           .csrf(csrf -> csrf.disable())
              .cors(cors -> cors.configurationSource(corsConfigurationSource()))
           .authorizeHttpRequests(authorize -> authorize
                   .requestMatchers("/auth/**").permitAll()
                   .requestMatchers("/api/auth/**").permitAll()
                   // Public job browsing - anyone can view jobs
                   .requestMatchers(HttpMethod.GET, "/api/jobs/**").permitAll()
                   .requestMatchers(HttpMethod.GET, "/api/companies/*/jobs").permitAll()

                   // Job management - only employers
                   .requestMatchers(HttpMethod.POST, "/api/jobs/**").hasRole("EMPLOYER")
                   .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasRole("EMPLOYER")
                   .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasRole("EMPLOYER")

                   // Application endpoints - only applicants can apply
                   .requestMatchers(HttpMethod.POST, "/api/applications/**").hasRole("APPLICANT")
                   .requestMatchers(HttpMethod.GET, "/api/applications/my-applications").hasRole("APPLICANT")

                   // Company management - only employers
                   .requestMatchers("/api/companies/**").hasRole("EMPLOYER")
                   .requestMatchers("/api/employer/**").hasRole("EMPLOYER")

                   // Applicant-specific endpoints
                   .requestMatchers("/api/applicant/**").hasRole("APPLICANT")

                   // Profile management - authenticated users
                   .requestMatchers("/api/profile/**").authenticated()
                   .requestMatchers("/api/user/**").authenticated()

                   // File upload/download endpoints
                   .requestMatchers(HttpMethod.POST, "/api/files/resume").hasRole("APPLICANT")
                   .requestMatchers(HttpMethod.GET, "/api/files/resume/**").authenticated()

                   // Admin endpoints (if you add admin role later)
                   .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .cors(Customizer.withDefaults())
           .authorizeHttpRequests(authorize -> authorize
                   .requestMatchers("/auth/**").permitAll()
                   .requestMatchers("/auth/login").permitAll()
                   .anyRequest().authenticated())
               .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider);
           http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",    // React dev server
                "http://localhost:5173",    // Vite dev server
                "http://localhost:8080",    // Alternative dev port
                "https://yourapp.com",      // Production frontend
                "https://www.yourapp.com"   // Production frontend with www
        ));;
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        corsConfiguration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
