package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping(value = "login")
    public ResponseEntity<BaseSuccessResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthUserResponse response = service.loginUser(request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }

    @PostMapping(value = "register")
    public ResponseEntity<BaseSuccessResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthUserResponse response = service.registerUser(request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }
}
