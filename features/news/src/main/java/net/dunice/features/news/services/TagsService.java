package net.dunice.features.news.services;

import net.dunice.features.shared.entities.TagEntity;
import java.util.List;

public interface TagsService {
    List<TagEntity> storeTagsAndGet(List<String> tags);
}
