package net.dunice.newsapi.services;

import jakarta.transaction.Transactional;
import net.dunice.newsapi.entities.TagEntity;
import java.util.List;

public interface TagsService {
    @Transactional
    List<TagEntity> storeTagsAndGet(List<String> tags);
}
