package net.dunice.features.auth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dunice.features.auth.dtos.requests.LoginRequest;
import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.auth.dtos.responses.UserResponse;
import net.dunice.features.auth.services.AuthService;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import net.dunice.features.core.dtos.responses.common.CustomSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping(value = "login")
    public ResponseEntity<BaseSuccessResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse response = service.loginUser(request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }

    @PostMapping(value = "register")
    public ResponseEntity<BaseSuccessResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = service.registerUser(request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }

    @GetMapping(value = "current")
    public ResponseEntity<BaseSuccessResponse> loadCurrentAuth() {
        UserDetails response = service.loadCurrentAuth();
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }
}
