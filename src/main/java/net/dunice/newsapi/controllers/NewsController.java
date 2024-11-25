package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.constants.ValidationConstants;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.NewsPagingResponse;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.ContentResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.entities.NewsEntity;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.services.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "news")
@AllArgsConstructor
@Valid
public class NewsController {
    private final NewsService service;

    @PostMapping
    public ResponseEntity<BaseSuccessResponse> createNews(
            @Valid
            @RequestBody
            NewsRequest request,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long id = service.createNews(request, user);
        return ResponseEntity.ok(new CustomSuccessResponse<>(id));
    }

    @GetMapping
    public ResponseEntity<BaseSuccessResponse> loadPaginatedNews(
            @RequestParam
            @Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID)
            Integer page,
            @RequestParam
            @Positive(message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Max(value = NewsEntity.MAX_PER_PAGE_NEWS, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
            Integer perPage
    ) {
        ContentResponse<NewsPagingResponse> response = service.loadAllPagingNews(page, perPage);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }
}
