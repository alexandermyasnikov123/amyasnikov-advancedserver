package net.dunice.newsapi.dtos.requests;

import net.dunice.newsapi.validations.ValidDescription;
import net.dunice.newsapi.validations.ValidNewsImage;
import net.dunice.newsapi.validations.ValidTag;
import net.dunice.newsapi.validations.ValidTitle;
import java.util.List;

public record NewsRequest(
        @ValidTitle
        String title,
        @ValidDescription
        String description,
        @ValidNewsImage
        String image,
        List<@ValidTag String> tags
) {
}
