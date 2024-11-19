package net.dunice.newsapi.repositories;

import net.dunice.newsapi.entities.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Integer> {
}
