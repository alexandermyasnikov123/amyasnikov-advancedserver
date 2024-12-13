package net.dunice.features.users.kafka;

import lombok.RequiredArgsConstructor;
import net.dunice.features.users.constants.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, List<String>> listKafkaTemplate;

    public CompletableFuture<SendResult<String, List<String>>> produceAvatarDeletion(String imageUrl) {
        return listKafkaTemplate.send(KafkaConstants.IMAGE_DELETION_TOPIC, List.of(imageUrl));
    }
}
