package net.dunice.features.news.kafka;

import lombok.RequiredArgsConstructor;
import net.dunice.features.news.constants.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ImageDeletionProducer {
    private final KafkaTemplate<String, List<String>> kafkaTemplate;

    public CompletableFuture<SendResult<String, List<String>>> produceImageDeleted(String imageUrl) {
        return produceImagesDeleted(List.of(imageUrl));
    }

    public CompletableFuture<SendResult<String, List<String>>> produceImagesDeleted(List<String> imageUrls) {
        return kafkaTemplate.send(KafkaConstants.DELETION_TOPIC, imageUrls);
    }
}
