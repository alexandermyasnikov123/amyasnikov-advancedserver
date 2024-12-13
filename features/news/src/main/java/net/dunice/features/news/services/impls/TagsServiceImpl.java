package net.dunice.features.news.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.news.entities.TagEntity;
import net.dunice.features.news.repositories.TagsRepository;
import net.dunice.features.news.services.TagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final TagsRepository repository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Set<TagEntity> storeTagsAndGet(List<String> tags) {
        Set<TagEntity> resultingTags = new HashSet<>();

        Set<String> existingTitles = repository.findAllByTitleIn(tags)
                .stream()
                .peek(resultingTags::add)
                .map(TagEntity::getTitle)
                .collect(Collectors.toSet());

        Set<TagEntity> nonExistingTags = tags.stream()
                .filter(title -> !existingTitles.contains(title))
                .map(title -> TagEntity.builder().title(title).build())
                .collect(Collectors.toSet());

        if (!nonExistingTags.isEmpty()) {
            resultingTags.addAll(repository.saveAll(nonExistingTags));
        }

        return resultingTags;
    }
}
