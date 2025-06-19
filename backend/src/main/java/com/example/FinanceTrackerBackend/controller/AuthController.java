package com.example.FinanceTrackerBackend.controller;

import com.example.FinanceTrackerBackend.model.dto.request.AuthenticationRequest;
import com.example.FinanceTrackerBackend.model.dto.response.AuthenticationResponse;
import com.example.FinanceTrackerBackend.model.dto.request.RegisterRequest;
import com.example.FinanceTrackerBackend.model.dto.response.ErrorResponse;
import com.example.FinanceTrackerBackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@ApiResponse(responseCode = "200", description = "Success")
@ApiResponse(responseCode = "400", description = "Client error - invalid input, json format", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
@Tag(name = "Authentication", description = "Register and login, provides JWT token")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Registers new user and returns JWS token")
    @ApiResponse(responseCode = "409", description = "Duplicate email", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @SecurityRequirements
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login existing user", description = "Provides JWT token")
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @SecurityRequirements
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
