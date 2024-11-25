package net.dunice.newsapi.repositories;

import lombok.NonNull;
import net.dunice.newsapi.entities.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Integer> {

    @Override
    @NonNull
    @EntityGraph(value = "news_graph_join_all", type = EntityGraph.EntityGraphType.FETCH)
    Page<NewsEntity> findAll(@NonNull Pageable pageable);
}
