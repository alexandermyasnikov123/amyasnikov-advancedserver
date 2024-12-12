package net.dunice.features.news.repositories;

import net.dunice.features.news.entities.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    @Query(value = """
            FROM NewsEntity ne
            JOIN FETCH ne.tags tag
            WHERE (
            LOWER(ne.title) LIKE LOWER(CONCAT('%', :keywords, '%'))
            OR LOWER(ne.description) LIKE LOWER(CONCAT('%', :keywords, '%'))
            )
            AND ne.authorUsername = COALESCE(:authorUsername, ne.authorUsername)
            AND (:tags IS NULL OR tag.title in (:tags))
            """)
    Page<NewsEntity> findAllByKeywords(
            String keywords,
            String authorUsername,
            List<String> tags,
            Pageable pageable
    );

    @Override
    @EntityGraph(value = "news_graph_join_all")
    Page<NewsEntity> findAll(Pageable pageable);

    @EntityGraph(value = "news_graph_join_all")
    Page<NewsEntity> findAllByAuthorId(UUID userId, Pageable pageable);

    void deleteAllByAuthorUsername(String username);
}
