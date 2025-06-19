package com.example.FinanceTrackerBackend.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    @Email(message = "Enter valid email")
    private String email;
    @NotBlank(message = "Password must not be empty")
    private String password;
}
