package com.ztrios.etarms.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztrios.etarms.common.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // Inject the Spring-managed ObjectMapper (with JavaTimeModule)
    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = ErrorResponse.of(
                401,
                "Authentication Failed",
                "Invalid or expired authentication token",
                request.getRequestURI()
        );

        // Use the injected ObjectMapper to serialize Instant properly
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
