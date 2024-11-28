package net.dunice.newsapi.services.constants;

import net.dunice.newsapi.entities.TagEntity;
import java.util.ArrayList;
import java.util.List;

public final class TagsTestDefaults {
    public static List<TagEntity> generateTags(Integer amount) {
        List<TagEntity> result = new ArrayList<>();
        for (var i = 1L; i <= amount; ++i) {
            result.add(TagEntity.builder().title("title " + i).build());
        }
        return result;
    }
}
