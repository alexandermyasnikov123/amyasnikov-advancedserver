package net.dunice.features.news.services;

import net.dunice.features.core.dtos.responses.ContentResponse;
import net.dunice.features.news.dtos.requests.NewsRequest;
import net.dunice.features.news.entities.NewsEntity;
import org.springframework.http.HttpHeaders;
import java.util.List;

public interface NewsService {
    ContentResponse<NewsEntity> loadAllPagingNews(Integer page, Integer perPage);

    ContentResponse<NewsEntity> findAllPagingNews(
            Integer page,
            Integer perPage,
            String author,
            String keywords,
            List<String> tags
    );

    ContentResponse<NewsEntity> findAllPagingNewsByUserUuid(
            Integer page,
            Integer perPage,
            String uuid
    );

    Long createNews(NewsRequest request, HttpHeaders headers);

    void updateNews(Long id, NewsRequest request, HttpHeaders headers);

    void deleteNews(Long id, HttpHeaders headers);

    void deleteAllUserNews(String username);
}
