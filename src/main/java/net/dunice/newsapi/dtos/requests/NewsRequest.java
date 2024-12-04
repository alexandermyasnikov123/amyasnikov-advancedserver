package net.dunice.newsapi.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.dunice.newsapi.constants.NewsValidationConstraints;
import net.dunice.newsapi.constants.UserValidationConstraints;
import net.dunice.newsapi.constants.ValidationMessages;
import java.util.List;

public record NewsRequest(
        @NotBlank(message = ValidationMessages.NEWS_TITLE_NOT_NULL)
        @Size(
                min = NewsValidationConstraints.MIN_TITLE_LENGTH,
                max = NewsValidationConstraints.MAX_TITLE_LENGTH,
                message = ValidationMessages.NEWS_TITLE_SIZE
        )
        String title,
        @NotBlank(message = ValidationMessages.NEWS_DESCRIPTION_NOT_NULL)
        @Size(
                min = UserValidationConstraints.MIN_DESCRIPTION_LENGTH,
                max = UserValidationConstraints.MAX_DESCRIPTION_LENGTH,
                message = ValidationMessages.NEWS_DESCRIPTION_SIZE
        )
        String description,
        @NotBlank(message = ValidationMessages.NEWS_IMAGE_HAS_TO_BE_PRESENT)
        @Size(
                min = NewsValidationConstraints.MIN_IMAGE_LENGTH,
                max = NewsValidationConstraints.MAX_IMAGE_LENGTH,
                message = ValidationMessages.NEWS_IMAGE_HAS_TO_BE_PRESENT
        )
        String image,
        List<@NotBlank(message = ValidationMessages.TAGS_NOT_VALID) String> tags
) {
}
