package net.dunice.newsapi.services.impls;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.NewsPagingResponse;
import net.dunice.newsapi.dtos.responses.common.ContentResponse;
import net.dunice.newsapi.entities.NewsEntity;
import net.dunice.newsapi.entities.TagEntity;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.mappers.NewsMapper;
import net.dunice.newsapi.repositories.NewsRepository;
import net.dunice.newsapi.services.NewsService;
import net.dunice.newsapi.services.TagsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

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
        List<TagEntity> tags = tagsService.storeTagsAndGet(request.tags());
        NewsEntity entity = repository.save(mapper.requestToEntity(request, owner, tags));
        return entity.getId();
    }

    @Override
    public ContentResponse<NewsPagingResponse> loadAllPagingNews(Integer page, Integer perPage) {
        Page<NewsEntity> newsPage = repository.findAll(PageRequest.of(page - 1, perPage));

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
                PageRequest.of(page - 1, perPage)
        );

        return mapPageToResponse(newsPage);
    }

    private ContentResponse<NewsPagingResponse> mapPageToResponse(Page<? extends NewsEntity> newsPage) {
        Long numberOfElements = newsPage.getTotalElements();
        List<NewsPagingResponse> content = newsPage.get()
                .map(mapper::entityToPagingResponse)
                .toList();

        return new ContentResponse<>(content, numberOfElements);
    }
}
