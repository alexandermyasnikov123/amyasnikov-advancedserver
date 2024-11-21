package net.dunice.newsapi.services.impls;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.NewsPagingResponse;
import net.dunice.newsapi.dtos.responses.common.ContentResponse;
import net.dunice.newsapi.entities.NewsEntity;
import net.dunice.newsapi.entities.TagEntity;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.NewsMapper;
import net.dunice.newsapi.repositories.NewsRepository;
import net.dunice.newsapi.services.NewsService;
import net.dunice.newsapi.services.TagsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsServiceImpl implements NewsService {
    NewsRepository repository;

    TagsService tagsService;

    NewsMapper mapper;

    @Transactional
    @Override
    public Long createNews(NewsRequest request, UserEntity owner) {
        return saveNewsAndTags(null, request, owner).getId();
    }

    @Transactional
    @Override
    public void updateNews(Long id, NewsRequest request, UserEntity owner) {
        runIfExistsOrElse(id, owner, ignored -> saveNewsAndTags(id, request, owner), () -> {
        });
    }

    @Override
    public void deleteNews(Long id, UserEntity owner) {
        runIfExistsOrElse(id, owner, repository::delete, () -> {
            throw new ErrorCodesException(ErrorCodes.NEWS_NOT_FOUND);
        });
    }

    @Override
    public ContentResponse<NewsPagingResponse> loadAllPagingNews(Integer page, Integer perPage) {
        Page<NewsEntity> newsPage = repository.findAll(buildPageRequest(page, perPage));

        return mapPageToResponse(newsPage);
    }

    @Override
    public ContentResponse<NewsPagingResponse> findAllPagingNews(
            Integer page,
            Integer perPage,
            @Nullable String author,
            @Nullable String keywords,
            @Nullable String[] tags
    ) {
        String lowerKeywords = keywords == null ? "" : keywords.toLowerCase();
        List<String> tagsOrNull = tags == null || tags.length < 1 ? null : Arrays.asList(tags);

        Page<NewsEntity> newsPage = repository.findAllByKeywords(
                lowerKeywords,
                author,
                tagsOrNull,
                buildPageRequest(page, perPage)
        );

        return mapPageToResponse(newsPage);
    }

    @Override
    public ContentResponse<NewsPagingResponse> findAllPagingNewsByUserUuid(
            Integer page,
            Integer perPage,
            String uuid
    ) {
        Page<NewsEntity> newsPage = repository.findAllByUser_Uuid(
                UUID.fromString(uuid),
                buildPageRequest(page, perPage)
        );

        return mapPageToResponse(newsPage);
    }

    private void runIfExistsOrElse(Long id, UserEntity owner, Consumer<NewsEntity> ifPresent, Runnable ifNotPresent) {
        repository.findById(id).ifPresentOrElse(news -> {
            if (news.getUser().getUuid().equals(owner.getUuid())) {
                ifPresent.accept(news);
            }
        }, ifNotPresent);
    }

    private NewsEntity saveNewsAndTags(Long id, NewsRequest request, UserEntity owner) {
        List<TagEntity> tags = tagsService.storeTagsAndGet(request.tags());
        return repository.save(mapper.requestToEntity(id, request, owner, tags));
    }

    private Pageable buildPageRequest(Integer page, Integer perPage) {
        return PageRequest.of(page - 1, perPage);
    }

    private ContentResponse<NewsPagingResponse> mapPageToResponse(Page<? extends NewsEntity> newsPage) {
        Long numberOfElements = newsPage.getTotalElements();
        List<NewsPagingResponse> content = newsPage.get()
                .map(mapper::entityToPagingResponse)
                .toList();

        return new ContentResponse<>(content, numberOfElements);
    }
}
