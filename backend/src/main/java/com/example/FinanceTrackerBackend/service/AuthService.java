package com.example.FinanceTrackerBackend.service;

import com.example.FinanceTrackerBackend.config.JwtService;
import com.example.FinanceTrackerBackend.exception.ApiException;
import com.example.FinanceTrackerBackend.exception.constant.ErrorCode;
import com.example.FinanceTrackerBackend.model.dto.request.AuthenticationRequest;
import com.example.FinanceTrackerBackend.model.dto.response.AuthenticationResponse;
import com.example.FinanceTrackerBackend.model.dto.request.RegisterRequest;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.EMAIL_DUPLICATE);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new AuthenticationResponse(
                jwtService.generateToken(user)
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = (User) auth.getPrincipal();
        return new AuthenticationResponse(
                jwtService.generateToken(user)
        );
    }
}
