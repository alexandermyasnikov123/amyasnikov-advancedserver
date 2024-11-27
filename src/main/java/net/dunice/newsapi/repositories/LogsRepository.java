package net.dunice.newsapi.repositories;

import net.dunice.newsapi.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository<LogEntity, Long> {
}
