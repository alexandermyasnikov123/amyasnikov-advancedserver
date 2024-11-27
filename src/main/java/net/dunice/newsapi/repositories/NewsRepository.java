package net.dunice.newsapi.repositories;

import net.dunice.newsapi.entities.NewsEntity;
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
            LEFT JOIN FETCH ne.author author
            LEFT JOIN FETCH ne.tags tags
            WHERE (
            LOWER(ne.title) LIKE %:keywords%
            OR LOWER(ne.description) LIKE %:keywords%
            )
            AND author.username = COALESCE(:author, author.username)
            AND (:tags IS NULL OR tags.title in (:tags))
            """)
    Page<NewsEntity> findAllByKeywords(String keywords, String author, List<String> tags, Pageable pageable);

    @Override
    @EntityGraph(value = "news_graph_join_all")
    Page<NewsEntity> findAll(Pageable pageable);

    @EntityGraph(value = "news_graph_join_all")
    Page<NewsEntity> findAllByAuthorId(UUID userId, Pageable pageable);
}
