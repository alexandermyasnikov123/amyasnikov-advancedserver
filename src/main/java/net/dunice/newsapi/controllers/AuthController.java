package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {

    @PostMapping(value = "login")
    public ResponseEntity<BaseSuccessResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(BaseSuccessResponse.success());
    }

    @PostMapping(value = "register")
    public ResponseEntity<BaseSuccessResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(BaseSuccessResponse.success());
    }
}
