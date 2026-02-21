package com.saurav.BlinkDeliver.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saurav.BlinkDeliver.dto.LoginDto;
import com.saurav.BlinkDeliver.utils.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!request.getServletPath().equals("/generate-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            LoginDto loginRequest = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authResult = authenticationManager.authenticate(authToken);
            String token = jwtUtil.generateToken(authResult.getName(), 15); // 15min
            response.setHeader("Authorization", "Bearer " + token);

            String refreshToken = jwtUtil.generateToken(authResult.getName(), 7 * 24 * 60); // 7day
            // Set Refresh Token in HttpOnly Cookie
            // we can also send it in response body but then client has to store it in local
            // storage or in-memory
            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true); // prevent javascript from accessing it
            refreshCookie.setSecure(false); // Allow on HTTP for development
            refreshCookie.setPath("/"); // Available for all paths (simplifies matching)
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiry
            response.addCookie(refreshCookie);

        } catch (BadCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication Failed");
            errorResponse.put("message", "Bad credentials");
            errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.put("timestamp", System.currentTimeMillis());
            response.getWriter().write(mapper.writeValueAsString(errorResponse));

        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication Failed");
            errorResponse.put("message", "Bad credentials");
            errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.put("timestamp", System.currentTimeMillis());
            response.getWriter().write(mapper.writeValueAsString(errorResponse));
        }
    }
}
