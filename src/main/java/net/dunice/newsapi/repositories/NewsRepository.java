package net.dunice.newsapi.repositories;

import lombok.NonNull;
import net.dunice.newsapi.entities.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Integer> {
    @Query(value = """
            FROM NewsEntity ne
            LEFT JOIN FETCH ne.user user
            LEFT JOIN FETCH ne.tags tags
            WHERE (
            LOWER(ne.title) LIKE %:keywords%
            OR LOWER(ne.description) LIKE %:keywords%
            )
            AND user.username = COALESCE(:author, user.username)
            AND (:tags IS NULL OR tags.title in (:tags))
            """)
    Page<NewsEntity> findAllByKeywords(String keywords, String author, List<String> tags, Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(value = "news_graph_join_all", type = EntityGraph.EntityGraphType.FETCH)
    Page<NewsEntity> findAll(@NonNull Pageable pageable);

    @EntityGraph(value = "news_graph_join_all", type = EntityGraph.EntityGraphType.FETCH)
    Page<NewsEntity> findAllByUser_Uuid(UUID uuid, Pageable pageable);
}
