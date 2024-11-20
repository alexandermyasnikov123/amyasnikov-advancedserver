package net.dunice.newsapi.repositories;

import net.dunice.newsapi.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<TagEntity, Integer> {
    List<TagEntity> findAllByTitleIn(List<String> titles);
}
