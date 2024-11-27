package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.constants.NewsValidationConstraints;
import net.dunice.newsapi.constants.ValidationMessages;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.ContentResponse;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.entities.NewsEntity;
import net.dunice.newsapi.services.NewsService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService service;

    @PostMapping
    public ResponseEntity<BaseSuccessResponse> createNews(
            @Valid
            @RequestBody
            NewsRequest request
    ) {
        Long id = service.createNews(request);
        return ResponseEntity.ok(new BaseSuccessResponse().addPayload("id", id));
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<BaseSuccessResponse> updateNews(
            @PathVariable
            @Positive(message = ValidationMessages.NEWS_ID_MUST_BE_POSITIVE)
            @NotNull(message = ValidationMessages.NEWS_ID_NULL)
            Long id,
            @Valid
            @RequestBody
            NewsRequest request
    ) {
        service.updateNews(id, request);
        return ResponseEntity.ok(new BaseSuccessResponse());
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<BaseSuccessResponse> deleteNews(
            @PathVariable
            @NotNull(message = ValidationMessages.NEWS_ID_NULL)
            Long id
    ) {
        service.deleteNews(id);
        return ResponseEntity.ok(new BaseSuccessResponse());
    }

    @GetMapping
    public ResponseEntity<BaseSuccessResponse> loadPaginatedNews(
            @RequestParam
            @Positive(message = ValidationMessages.PAGE_SIZE_NOT_VALID)
            Integer page,
            @RequestParam
            @Positive(message = ValidationMessages.PER_PAGE_MIN_NOT_VALID)
            @Max(
                    value = NewsValidationConstraints.MAX_PER_PAGE_NEWS,
                    message = ValidationMessages.TASKS_PER_PAGE_LESS_OR_EQUAL_100
            )
            Integer perPage
    ) {
        ContentResponse<NewsEntity> response = service.loadAllPagingNews(page, perPage);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }

    @GetMapping(value = "find")
    public ResponseEntity<BaseSuccessResponse> loadPaginatedNewsBy(
            @RequestParam
            @Positive(message = ValidationMessages.PAGE_SIZE_NOT_VALID)
            Integer page,
            @RequestParam
            @Positive(message = ValidationMessages.PER_PAGE_MIN_NOT_VALID)
            @Max(
                    value = NewsValidationConstraints.MAX_PER_PAGE_NEWS,
                    message = ValidationMessages.TASKS_PER_PAGE_LESS_OR_EQUAL_100
            )
            Integer perPage,
            @RequestParam(required = false)
            String author,
            @RequestParam(required = false)
            String keywords,
            @RequestParam(required = false)
            List<@NotBlank(message = ValidationMessages.TAGS_NOT_VALID) String> tags
    ) {
        ContentResponse<NewsEntity> response = service.findAllPagingNews(page,
                perPage,
                author,
                keywords,
                tags
        );
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity<BaseSuccessResponse> loadSpecificNews(
            @PathVariable
            @UUID(message = ValidationMessages.MAX_UPLOAD_SIZE_EXCEEDED)
            String userId,
            @RequestParam
            @Positive(message = ValidationMessages.PAGE_SIZE_NOT_VALID)
            Integer page,
            @RequestParam
            @Positive(message = ValidationMessages.PER_PAGE_MIN_NOT_VALID)
            @Max(
                    value = NewsValidationConstraints.MAX_PER_PAGE_NEWS,
                    message = ValidationMessages.TASKS_PER_PAGE_LESS_OR_EQUAL_100
            )
            Integer perPage
    ) {
        ContentResponse<NewsEntity> response = service.findAllPagingNewsByUserUuid(page, perPage, userId);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }
}
