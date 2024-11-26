package net.dunice.newsapi.dtos.responses;

import java.util.List;

public record ContentResponse<T>(
        List<T> content,
        Long numberOfElements
) {
}
