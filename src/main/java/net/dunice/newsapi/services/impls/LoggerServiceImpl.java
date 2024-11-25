package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.newsapi.entities.LogEntity;
import net.dunice.newsapi.repositories.LogsRepository;
import net.dunice.newsapi.services.LoggerService;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class LoggerServiceImpl implements LoggerService {
    private final LogsRepository repository;

    @Transactional
    @Override
    public void saveLog(String message, String level) {
        repository.save(LogEntity.builder()
                .date(new Date())
                .logger(log.getName())
                .level(level)
                .message(message)
                .build()
        );
    }
}
