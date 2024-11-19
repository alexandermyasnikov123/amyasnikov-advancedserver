package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.entities.NewsEntity;
import net.dunice.newsapi.entities.TagEntity;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.mappers.NewsMapper;
import net.dunice.newsapi.repositories.NewsRepository;
import net.dunice.newsapi.services.NewsService;
import net.dunice.newsapi.services.TagsService;
import org.springframework.stereotype.Service;
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
}
