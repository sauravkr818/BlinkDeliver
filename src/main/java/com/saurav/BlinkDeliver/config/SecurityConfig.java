package com.saurav.BlinkDeliver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.saurav.BlinkDeliver.authenticationProviders.JWTAuthenticationProvider;
import com.saurav.BlinkDeliver.filters.JWTAuthenticationFilter;
import com.saurav.BlinkDeliver.filters.JWTRefreshFilter;
import com.saurav.BlinkDeliver.filters.JwtValidationFilter;
import com.saurav.BlinkDeliver.utils.JWTUtil;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JWTUtil jwtUtil, UserDetailsService userDetailsService,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(jwtUtil, userDetailsService);
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Strength of 12 for better security
    }

    /**
     * Security filter chain configuration
     * 
     * @param http HttpSecurity object to configure
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager,
            JWTUtil jwtUtil)
            throws Exception {

        // Authentication filter responsible for login
        JWTAuthenticationFilter jwtAuthFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtil);

        // Validation filter for checking JWT in every request
        JwtValidationFilter jwtValidationFilter = new JwtValidationFilter(authenticationManager);

        // refresh filter for checking JWT in every request
        JWTRefreshFilter jwtRefreshFilter = new JWTRefreshFilter(jwtUtil, authenticationManager);

        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/api/users").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/products/**", "/brands/**", "/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/brands/**", "/categories/**")
                        .hasAnyRole("ADMIN", "BRAND_OWNER")
                        .requestMatchers(HttpMethod.PUT, "/brands/**", "/categories/**")
                        .hasAnyRole("ADMIN", "BRAND_OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/brands/**", "/categories/**")
                        .hasAnyRole("ADMIN", "BRAND_OWNER")
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/refresh-token").permitAll()
                        .requestMatchers("/wishlist/**").authenticated()
                        .requestMatchers("/orders/**").authenticated()
                        .requestMatchers("/payment/**").authenticated()
                        .requestMatchers("/addresses/**").authenticated()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // generate token filter
                .addFilterAfter(jwtValidationFilter, JWTAuthenticationFilter.class) // validate token filter
                .addFilterAfter(jwtRefreshFilter, JwtValidationFilter.class); // refresh token filter
        // .headers(headers -> headers

        // .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Allow H2 console
        // frames
        // );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(
                daoAuthenticationProvider(),
                jwtAuthenticationProvider()));
    }

    /**
     * CORS configuration
     * 
     * @return CorsConfigurationSource for handling cross-origin requests
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}