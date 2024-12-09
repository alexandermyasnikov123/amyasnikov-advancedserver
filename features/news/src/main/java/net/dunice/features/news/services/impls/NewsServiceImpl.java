package net.dunice.features.news.services.impls;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.core.dtos.responses.ContentResponse;
import net.dunice.features.news.dtos.requests.NewsRequest;
import net.dunice.features.news.services.NewsService;
import net.dunice.features.news.services.TagsService;
import net.dunice.features.news.repositories.NewsRepository;
import net.dunice.features.news.mappers.NewsMapper;
import net.dunice.features.shared.entities.NewsEntity;
import net.dunice.features.shared.entities.TagEntity;
import net.dunice.features.shared.entities.UserEntity;
import net.dunice.features.users.utils.AuthenticationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repository;

    private final TagsService tagsService;

    private final NewsMapper mapper;

    @Override
    public Long createNews(NewsRequest request) {
        UserEntity owner = AuthenticationUtils.getCurrentUser();
        return saveNewsAndTags(0L, request, owner).getId();
    }

    @Override
    public void updateNews(Long id, NewsRequest request) {
        UserEntity owner = AuthenticationUtils.getCurrentUser();
        acceptIfCurrentUserIsAuthor(id, owner, ignored -> saveNewsAndTags(id, request, owner));
    }

    @Override
    public void deleteNews(Long id) {
        UserEntity owner = AuthenticationUtils.getCurrentUser();
        acceptIfCurrentUserIsAuthor(id, owner, repository::delete);
    }

    @Override
    public ContentResponse<NewsEntity> loadAllPagingNews(Integer page, Integer perPage) {
        Page<NewsEntity> newsPage = repository.findAll(PageRequest.of(page - 1, perPage));

        return mapPageToResponse(newsPage);
    }

    @Override
    public ContentResponse<NewsEntity> findAllPagingNews(
            Integer page,
            Integer perPage,
            @Nullable String author,
            @Nullable String keywords,
            @Nullable List<String> tags
    ) {
        String lowerKeywords = keywords == null ? "" : keywords.toLowerCase();
        List<String> tagsOrNull = tags == null || tags.isEmpty() ? null : tags;

        Page<NewsEntity> newsPage = repository.findAllByKeywords(
                lowerKeywords,
                author,
                tagsOrNull,
                PageRequest.of(page - 1, perPage)
        );

        return mapPageToResponse(newsPage);
    }

    @Override
    public ContentResponse<NewsEntity> findAllPagingNewsByUserUuid(
            Integer page,
            Integer perPage,
            String uuid
    ) {
        Page<NewsEntity> newsPage = repository.findAllByAuthorId(
                UUID.fromString(uuid),
                PageRequest.of(page - 1, perPage)
        );

        return mapPageToResponse(newsPage);
    }

    private void acceptIfCurrentUserIsAuthor(Long newsId, UserEntity currentUser, Consumer<NewsEntity> ifAuthor) {
        repository.findById(newsId).ifPresentOrElse(news -> {
            if (!currentUser.equals(news.getAuthor())) {
                throw new ErrorCodesException(ErrorCodes.CANT_MODIFY_FOREIGN_NEWS);
            }
            ifAuthor.accept(news);
        }, () -> {
            throw new ErrorCodesException(ErrorCodes.NEWS_NOT_FOUND);
        });
    }

    private NewsEntity saveNewsAndTags(Long id, NewsRequest request, UserEntity owner) {
        List<TagEntity> tags = tagsService.storeTagsAndGet(request.tags());
        return repository.save(
                mapper.requestToEntity(id, request)
                        .setTags(tags)
                        .setAuthor(owner)
        );
    }

    private ContentResponse<NewsEntity> mapPageToResponse(Page<NewsEntity> newsPage) {
        Long numberOfElements = newsPage.getTotalElements();
        List<NewsEntity> content = newsPage.getContent();

        return new ContentResponse<>(content, numberOfElements);
    }
}
