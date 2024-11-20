package net.dunice.newsapi.dtos.responses;

import net.dunice.newsapi.entities.TagEntity;
import java.util.UUID;

public record NewsPagingResponse(
        Integer id,
        String title,
        String description,
        String image,
        Iterable<TagEntity> tags,
        UUID userId,
        String username
) {
}
