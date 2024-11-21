package net.dunice.newsapi.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.constants.ValidationConstants;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.NewsPagingResponse;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.ContentResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.services.NewsService;
import net.dunice.newsapi.validations.ValidPage;
import net.dunice.newsapi.validations.ValidPerPage;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "news")
@AllArgsConstructor
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
        return ResponseEntity.ok(
                new BaseSuccessResponse(ErrorCodes.SUCCESS.getStatusCode(), null, null) {
                    public Long getId() {
                        return id;
                    }
                }
        );
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<BaseSuccessResponse> updateNews(
            @PathVariable
            @NotNull(message = ValidationConstants.NEWS_ID_NULL)
            Long id,
            @Valid
            @RequestBody
            NewsRequest request,
            Authentication authentication
    ) {
        service.updateNews(id, request, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(BaseSuccessResponse.success());
    }

    @GetMapping
    public ResponseEntity<BaseSuccessResponse> loadPaginatedNews(
            @RequestParam
            @ValidPage
            Integer page,
            @RequestParam
            @ValidPerPage
            Integer perPage
    ) {
        ContentResponse<NewsPagingResponse> response = service.loadAllPagingNews(page, perPage);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }

    @GetMapping(value = "find")
    public ResponseEntity<BaseSuccessResponse> loadPaginatedNewsBy(
            @RequestParam
            @ValidPage
            Integer page,
            @RequestParam
            @ValidPerPage
            Integer perPage,
            String author,
            String keywords,
            String[] tags
    ) {
        ContentResponse<NewsPagingResponse> response = service.findAllPagingNews(page, perPage, author, keywords, tags);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }

    @GetMapping(value = "{userId}")
    public ResponseEntity<BaseSuccessResponse> loadSpecificNews(
            @PathVariable
            @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED)
            String userId,
            @RequestParam
            @ValidPage
            Integer page,
            @RequestParam
            @ValidPerPage
            Integer perPage
    ) {

        ContentResponse<NewsPagingResponse> response = service.findAllPagingNewsByUserUuid(page, perPage, userId);
        return ResponseEntity.ok(new CustomSuccessResponse<>(response));
    }
}
