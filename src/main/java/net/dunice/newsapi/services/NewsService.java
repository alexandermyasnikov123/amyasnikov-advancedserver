package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.dtos.responses.NewsPagingResponse;
import net.dunice.newsapi.dtos.responses.common.ContentResponse;
import net.dunice.newsapi.entities.UserEntity;

public interface NewsService {
    Long createNews(NewsRequest request, UserEntity owner);

    ContentResponse<NewsPagingResponse> loadAllPagingNews(Integer page, Integer perPage);

    ContentResponse<NewsPagingResponse> findAllPagingNews(
            Integer page,
            Integer perPage,
            String author,
            String keywords,
            String[] tags
    );

    ContentResponse<NewsPagingResponse> findAllPagingNewsByUserUuid(
            Integer page,
            Integer perPage,
            String uuid
    );
}
