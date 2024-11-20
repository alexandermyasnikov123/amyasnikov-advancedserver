package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.entities.TagEntity;
import net.dunice.newsapi.repositories.TagsRepository;
import net.dunice.newsapi.services.TagsService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final TagsRepository repository;

    @Transactional
    @Override
    public List<TagEntity> storeTagsAndGet(List<String> tags) {
        List<TagEntity> resultingTags = new ArrayList<>();

        List<String> existingTitles = repository.findAllByTitleIn(tags)
                .stream()
                .peek(resultingTags::add)
                .map(TagEntity::getTitle)
                .toList();

        List<TagEntity> nonExistingTags = tags.stream()
                .filter(title -> !existingTitles.contains(title))
                .map(title -> TagEntity.builder().title(title).build())
                .toList();

        resultingTags.addAll(repository.saveAll(nonExistingTags));

        return resultingTags;
    }
}
