package net.dunice.newsapi.controllers;

import lombok.AllArgsConstructor;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.DataResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(value = "user")
@AllArgsConstructor
public class UserController {
    private final UserAuthService service;

    @GetMapping
    public ResponseEntity<BaseSuccessResponse> loadAllUsers() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(service.loadAllUsers()));
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<BaseSuccessResponse> loadUserById(@PathVariable UUID uuid) {
        PublicUserResponse userResponse = service.loadUserByUuid(uuid);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @GetMapping(value = "info")
    public ResponseEntity<BaseSuccessResponse> loadCurrentUserInfo(Authentication authentication) {
        return loadUserById(((UserEntity) authentication.getPrincipal()).getUuid());
    }

    @PutMapping
    public ResponseEntity<BaseSuccessResponse> updateCurrentUser(
            Authentication authentication,
            @RequestBody UpdateUserRequest request
    ) {
        UUID uuid = ((UserEntity) authentication.getPrincipal()).getUuid();
        DataResponse<PublicUserResponse> userResponse = service.updateUser(uuid, request);
        return ResponseEntity.ok(new CustomSuccessResponse<>(userResponse));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteCurrentUser(Principal principal) {
        service.deleteUserByUsername(principal.getName());
        return ResponseEntity.ok(BaseSuccessResponse.success());
    }
}
