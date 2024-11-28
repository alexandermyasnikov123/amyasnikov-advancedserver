package net.dunice.newsapi.services.impls;

import net.dunice.newsapi.BaseTestCase;
import net.dunice.newsapi.entities.TagEntity;
import net.dunice.newsapi.repositories.TagsRepository;
import net.dunice.newsapi.services.constants.TagsTestDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagsServiceImplTest extends BaseTestCase {
    private final List<TagEntity> allTags = TagsTestDefaults.generateTags(10);

    private final List<String> allTagsTitles = allTags.stream()
            .map(TagEntity::getTitle)
            .toList();

    @Mock
    private TagsRepository repository;

    private TagsServiceImpl service;

    @BeforeEach
    public void beforeEach() {
        service = new TagsServiceImpl(repository);
    }

    @Test
    public void storeTagsAndGet_ReturnsAllValuesIfExistsAndDoNotSaveAny() {
        when(repository.findAllByTitleIn(allTagsTitles)).thenReturn(allTags);

        var actual = service.storeTagsAndGet(allTagsTitles);

        verify(repository).findAllByTitleIn(allTagsTitles);
        verify(repository, never()).saveAll(allTags);

        Assertions.assertIterableEquals(allTags, actual);
    }

    @Test
    public void storeTagsAndGet_InsertAllTagsWhenValuesDoNotExists() {
        when(repository.findAllByTitleIn(allTagsTitles)).thenReturn(List.of());
        when(repository.saveAll(allTags)).thenReturn(allTags);

        var actual = service.storeTagsAndGet(allTagsTitles);

        verify(repository).saveAll(allTags);

        Assertions.assertIterableEquals(allTags, actual);
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    public void storeTagsAndGet_InsertsOnlyNonExistingTags() {
        Integer skipAmount = 3;

        List<TagEntity> nonExistingTags = allTags.stream().skip(skipAmount).toList();
        List<TagEntity> alreadyExistingTags = allTags.stream().limit(skipAmount).toList();

        when(repository.findAllByTitleIn(allTagsTitles)).thenReturn(alreadyExistingTags);
        when(repository.saveAll(nonExistingTags)).thenReturn(nonExistingTags);

        var actual = service.storeTagsAndGet(allTagsTitles);

        verify(repository).saveAll(nonExistingTags);

        Assertions.assertIterableEquals(allTags, actual);
    }
}