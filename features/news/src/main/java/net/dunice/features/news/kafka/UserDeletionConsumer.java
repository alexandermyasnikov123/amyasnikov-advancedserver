package net.dunice.features.news.kafka;

import lombok.RequiredArgsConstructor;
import net.dunice.features.news.constants.KafkaConstants;
import net.dunice.features.news.services.NewsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeletionConsumer {
    private final NewsService newsService;

    @KafkaListener(topics = KafkaConstants.DELETION_TOPIC, groupId = KafkaConstants.DELETION_GROUP)
    public void onUserDeleted(String username) {
        newsService.deleteAllUserNews(username);
    }
}
