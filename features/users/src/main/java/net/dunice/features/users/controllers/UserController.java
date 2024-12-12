package net.dunice.features.users.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.features.core.dtos.constants.ValidationMessages;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import net.dunice.features.core.dtos.responses.common.CustomSuccessResponse;
import net.dunice.features.users.dtos.requests.UserRequest;
import net.dunice.features.users.dtos.responses.UserResponse;
import net.dunice.features.users.services.UserService;
import net.dunice.features.users.validation.ValidEmail;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
        UserResponse userResponse = service.loadUserByUuid(java.util.UUID.fromString(uuid));
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @GetMapping(value = "email/{email}")
    public ResponseEntity<BaseSuccessResponse> loadUserByEmail(
            @PathVariable
            @ValidEmail
            String email
    ) {
        UserResponse userResponse = service.loadUserByEmail(email);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @GetMapping(value = "info")
    public ResponseEntity<BaseSuccessResponse> loadCurrentUserInfo(@RequestHeader HttpHeaders headers) {
        UserResponse userResponse = service.loadCurrent(headers);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @PutMapping
    public ResponseEntity<BaseSuccessResponse> updateCurrentUser(
            @Valid @RequestBody UserRequest request,
            @RequestHeader HttpHeaders headers
    ) {
        UserResponse userResponse = service.updateUser(headers, request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @PostMapping
    public ResponseEntity<BaseSuccessResponse> insertUser(@Valid @RequestBody UserRequest request) {
        UserResponse userResponse = service.insertUser(request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteCurrentUser(@RequestHeader HttpHeaders headers) {
        service.deleteUser(headers);
        return ResponseEntity.ok(new BaseSuccessResponse());
    }
}
