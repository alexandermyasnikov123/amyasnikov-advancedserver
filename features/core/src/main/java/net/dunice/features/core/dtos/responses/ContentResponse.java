package net.dunice.features.core.dtos.responses;

import java.util.List;

public record ContentResponse<T>(
        List<T> content,
        Long numberOfElements
) {
}
