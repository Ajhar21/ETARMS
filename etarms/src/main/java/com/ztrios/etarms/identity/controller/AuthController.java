package com.ztrios.etarms.identity.controller;

import com.ztrios.etarms.identity.dto.AuthRequest;
import com.ztrios.etarms.identity.dto.AuthResponse;
import com.ztrios.etarms.identity.security.CustomUserDetailsService;
import com.ztrios.etarms.identity.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        try {
            // Authenticate credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // Generate JWT
            String token = jwtProvider.generateToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().iterator().next().getAuthority()
            );

            // Return response
            return ResponseEntity.ok(
                    new AuthResponse(token, userDetails.getUsername(),
                            userDetails.getAuthorities().iterator().next().getAuthority())
            );

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
    }
}
