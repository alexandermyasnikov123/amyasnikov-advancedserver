package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.constants.ValidationMessages;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.services.UserService;
import net.dunice.newsapi.utils.AuthenticationUtils;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<BaseSuccessResponse> loadAllUsers() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(service.loadAllUsers()));
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<BaseSuccessResponse> loadUserById(
            @PathVariable
            @UUID(message = ValidationMessages.MAX_UPLOAD_SIZE_EXCEEDED)
            String uuid
    ) {
        PublicUserResponse userResponse = service.loadUserByUuid(uuid);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @GetMapping(value = "info")
    public ResponseEntity<BaseSuccessResponse> loadCurrentUserInfo(Authentication authentication) {
        return loadUserById(extractUuidFrom(authentication));
    }

    @PutMapping
    public ResponseEntity<BaseSuccessResponse> updateCurrentUser(
            Authentication authentication,
            @Valid
            @RequestBody
            UpdateUserRequest request
    ) {
        String uuid = extractUuidFrom(authentication);
        PublicUserResponse userResponse = service.updateUser(uuid, request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteCurrentUser(Authentication authentication) {
        service.deleteUser(authentication);
        return ResponseEntity.ok(new BaseSuccessResponse());
    }

    private String extractUuidFrom(Authentication authentication) {
        return AuthenticationUtils.getUser(authentication).getId().toString();
    }
}
