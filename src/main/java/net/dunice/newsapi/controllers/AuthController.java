package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.UserResponse;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AuthController.ENDPOINT)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    public static final String ENDPOINT = "auth";

    private final UserAuthService service;

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
}
