package net.dunice.newsapi.services.impls;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.entities.TagEntity;
import net.dunice.newsapi.repositories.TagsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagsServiceImplTest {
    List<TagEntity> allTags = generateTags(10);

    List<String> allTitles = allTags.stream()
            .map(TagEntity::getTitle)
            .toList();

    @NonFinal
    TagsRepository repository;

    @NonFinal
    TagsServiceImpl service;

    @BeforeEach
    public void beforeEach() {
        repository = Mockito.mock(TagsRepository.class);
        service = new TagsServiceImpl(repository);
    }

    @Test
    public void storeTagsAndGet_ReturnsAllValuesIfExistsAndDoNotSaveAny() {
        Mockito.when(repository.findAllByTitleIn(Mockito.anyList())).thenReturn(allTags);
        Mockito.when(repository.saveAll(Mockito.any())).then(invocation -> Assertions.fail());

        var actual = service.storeTagsAndGet(allTitles);
        Assertions.assertIterableEquals(allTags, actual);
    }

    @Test
    public void storeTagsAndGet_InsertAllTagsWhenValuesDoNotExists() {
        AtomicBoolean saveAllWasCalled = new AtomicBoolean(false);

        Mockito.when(repository.findAllByTitleIn(Mockito.anyList())).thenReturn(List.of());
        Mockito.when(repository.saveAll(Mockito.any())).then(invocation -> {
            saveAllWasCalled.set(true);
            return allTags;
        });

        var actual = service.storeTagsAndGet(allTitles);

        Assertions.assertTrue(saveAllWasCalled.get());
        Assertions.assertIterableEquals(allTags, actual);
    }

    @SuppressWarnings({"unchecked", "WrapperTypeMayBePrimitive"})
    @Test
    public void storeTagsAndGet_InsertsOnlyNonExistingTags() {
        Integer skipAmount = 3;
        AtomicBoolean saveAllWasCalled = new AtomicBoolean(false);

        List<TagEntity> nonExistingTags = allTags.stream().skip(skipAmount).toList();
        List<TagEntity> alreadyExistingTags = allTags.stream().limit(skipAmount).toList();

        List<String> nonExistingTitles = nonExistingTags.stream().map(TagEntity::getTitle).toList();

        Mockito.when(repository.findAllByTitleIn(Mockito.anyList())).thenReturn(alreadyExistingTags);
        Mockito.when(repository.saveAll(Mockito.any())).then(invocation -> {
            saveAllWasCalled.set(true);
            var actualTags = (List<TagEntity>) invocation.getArgument(0);
            var actualTitles = actualTags.stream().map(TagEntity::getTitle).toList();

            Assertions.assertIterableEquals(nonExistingTitles, actualTitles);
            return nonExistingTags;
        });

        var actual = service.storeTagsAndGet(allTitles);
        Assertions.assertIterableEquals(allTags, actual);
        Assertions.assertTrue(saveAllWasCalled.get());
    }

    private List<TagEntity> generateTags(Integer amount) {
        List<TagEntity> result = new ArrayList<>();
        for (var i = 1L; i <= amount; ++i) {
            result.add(new TagEntity(i, "title_" + i));
        }
        return result;
    }
}