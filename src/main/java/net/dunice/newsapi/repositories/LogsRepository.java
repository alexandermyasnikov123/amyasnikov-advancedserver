package net.dunice.newsapi.repositories;

import net.dunice.newsapi.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends JpaRepository<LogEntity, Integer> {
}
