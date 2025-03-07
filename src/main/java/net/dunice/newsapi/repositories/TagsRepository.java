package net.dunice.newsapi.repositories;

import net.dunice.newsapi.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TagsRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findAllByTitleIn(List<String> titles);
}
