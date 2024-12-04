package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.ContentResponse;
import net.dunice.newsapi.entities.NewsEntity;
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

    Long createNews(NewsRequest request);

    void updateNews(Long id, NewsRequest request);

    void deleteNews(Long id);
}
