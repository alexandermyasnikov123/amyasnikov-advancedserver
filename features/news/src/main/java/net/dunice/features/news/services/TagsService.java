package net.dunice.features.news.services;

import net.dunice.features.news.entities.TagEntity;
import java.util.List;
import java.util.Set;

public interface TagsService {
    Set<TagEntity> storeTagsAndGet(List<String> tags);
}
