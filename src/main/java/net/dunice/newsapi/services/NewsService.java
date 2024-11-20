package net.dunice.newsapi.services;

import jakarta.transaction.Transactional;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.entities.UserEntity;

public interface NewsService {
    @Transactional
    Long createNews(NewsRequest request, UserEntity owner);
}
