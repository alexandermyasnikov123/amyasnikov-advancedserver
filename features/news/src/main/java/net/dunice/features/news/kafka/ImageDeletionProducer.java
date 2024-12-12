package net.dunice.features.news.kafka;

import lombok.RequiredArgsConstructor;
import net.dunice.features.news.constants.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ImageDeletionProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> produceImageDeleted(String imageUrl) {
        return kafkaTemplate.send(KafkaConstants.DELETION_TOPIC, imageUrl);
    }
}
