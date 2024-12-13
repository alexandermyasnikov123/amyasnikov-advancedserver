package net.dunice.features.news.services.impls;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.core.dtos.responses.ContentResponse;
import net.dunice.features.core.dtos.utils.ResponseUtils;
import net.dunice.features.news.clients.AuthApiClient;
import net.dunice.features.news.dtos.reponses.CredentialsResponse;
import net.dunice.features.news.dtos.requests.NewsRequest;
import net.dunice.features.news.entities.NewsEntity;
import net.dunice.features.news.entities.TagEntity;
import net.dunice.features.news.kafka.ImageDeletionProducer;
import net.dunice.features.news.mappers.NewsMapper;
import net.dunice.features.news.repositories.NewsRepository;
import net.dunice.features.news.services.NewsService;
import net.dunice.features.news.services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repository;

    private final TagsService tagsService;

    private final NewsMapper mapper;

    private final AuthApiClient apiClient;

    private final ImageDeletionProducer eventProducer;

    @Lazy
    @Autowired
    private NewsServiceImpl service;

    @Override
    public Long createNews(NewsRequest request, HttpHeaders headers) {
        CredentialsResponse ownerCredentials = loadCurrentUser(headers);
        return service.saveNewsAndTags(null, request, ownerCredentials).getId();
    }

    @Override
    public void updateNews(Long id, NewsRequest request, HttpHeaders headers) {
        CredentialsResponse ownerCredentials = loadCurrentUser(headers);
        acceptIfCurrentUserIsAuthor(
                id,
                ownerCredentials,
                oldEntity -> {
                    NewsEntity newEntity = service.saveNewsAndTags(id, request, ownerCredentials);
                    String oldImage = oldEntity.getImage();
                    if (!newEntity.getImage().equals(oldImage)) {
                        eventProducer.produceImageDeleted(oldImage);
                    }
                }
        );
    }

    @Override
    @Transactional
    public void deleteNews(Long id, HttpHeaders headers) {
        CredentialsResponse ownerCredentials = loadCurrentUser(headers);
        acceptIfCurrentUserIsAuthor(id, ownerCredentials, newsEntity -> {
            repository.delete(newsEntity);
            eventProducer.produceImageDeleted(newsEntity.getImage());
        });
    }

    @Override
    @Transactional
    public void deleteAllUserNews(String username) {
        repository.deleteAllByAuthorUsername(username);
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public NewsEntity saveNewsAndTags(Long id, NewsRequest request, CredentialsResponse credentials) {
        Set<TagEntity> tags = tagsService.storeTagsAndGet(request.tags());

        return repository.save(
                mapper.requestToEntity(id, request, credentials).setTags(tags)
        );
    }

    private void acceptIfCurrentUserIsAuthor(
            Long newsId,
            CredentialsResponse ownerCredentials,
            Consumer<NewsEntity> ifAuthor
    ) {
        repository.findById(newsId).ifPresentOrElse(news -> {
            if (!ownerCredentials.username().equals(news.getAuthorUsername())) {
                throw new ErrorCodesException(ErrorCodes.CANT_MODIFY_FOREIGN_NEWS);
            }
            ifAuthor.accept(news);
        }, () -> {
            throw new ErrorCodesException(ErrorCodes.NEWS_NOT_FOUND);
        });
    }

    private ContentResponse<NewsEntity> mapPageToResponse(Page<NewsEntity> newsPage) {
        Long numberOfElements = newsPage.getTotalElements();
        List<NewsEntity> content = newsPage.getContent();

        return new ContentResponse<>(content, numberOfElements);
    }

    private CredentialsResponse loadCurrentUser(HttpHeaders headers) {
        return ResponseUtils.tryExtractData(apiClient.loadCurrentPrincipal(headers));
    }
}
