package net.dunice.features.auth.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.features.auth.constants.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> produceUserDeleted(String username) {
        return kafkaTemplate.send(KafkaConstants.DELETION_TOPIC, username)
                .whenComplete((sendResult, throwable) -> {
                    if (throwable == null) {
                        log.info("Message successfully sent: {}", sendResult);
                    } else {
                        log.error("Exception occurred: {}, kafka send result: {}", throwable.getMessage(), sendResult);
                    }
                });
    }
}
