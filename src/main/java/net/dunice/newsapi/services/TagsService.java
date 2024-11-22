package net.dunice.newsapi.services;

import net.dunice.newsapi.entities.TagEntity;
import java.util.List;

public interface TagsService {
    List<TagEntity> storeTagsAndGet(List<String> tags);
}
