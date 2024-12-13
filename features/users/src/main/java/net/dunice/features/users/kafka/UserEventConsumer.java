package net.dunice.features.users.kafka;

import lombok.RequiredArgsConstructor;
import net.dunice.features.users.constants.KafkaConstants;
import net.dunice.features.users.services.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventConsumer {
    private final UserService userService;

    @KafkaListener(topics = KafkaConstants.DELETION_TOPIC, groupId = KafkaConstants.DELETION_GROUP)
    void onUserDeletion(String username) {
        userService.deleteUserByUsername(username);
    }
}
