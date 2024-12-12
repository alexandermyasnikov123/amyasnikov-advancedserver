package net.dunice.features.users.kafka;

import lombok.RequiredArgsConstructor;
import net.dunice.features.users.constants.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserImageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> produceImageDeletion(String imageUrl) {
        return kafkaTemplate.send(KafkaConstants.IMAGE_DELETION_TOPIC, imageUrl);
    }
}
