package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.services.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "news")
@AllArgsConstructor
public class NewsController {
    private final NewsService service;

    @PostMapping
    public ResponseEntity<BaseSuccessResponse> createNews(
            @Valid @RequestBody NewsRequest request,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long id = service.createNews(request, user);
        return ResponseEntity.ok(new CustomSuccessResponse<>(id));
    }
}
