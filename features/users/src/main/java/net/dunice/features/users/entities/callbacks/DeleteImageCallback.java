package net.dunice.features.users.entities.callbacks;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import net.dunice.features.users.entities.UserEntity;
import net.dunice.features.users.kafka.UserEventProducer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DeleteImageCallback {
    private final UserEventProducer eventProducer;

    private volatile String cachedImage;

    @PostLoad
    public void cacheAvatar(UserEntity entity) {
        cachedImage = entity.getAvatar();
    }

    @PostUpdate
    public void deleteOldAvatarIfChanged(UserEntity entity) {
        if (cachedImage != null && !cachedImage.equals(entity.getAvatar())) {
            eventProducer.produceAvatarDeletion(cachedImage);
        }
        cachedImage = entity.getAvatar();
    }

    @PostRemove
    public void deleteAvatar(UserEntity entity) {
        eventProducer.produceAvatarDeletion(entity.getAvatar());
    }
}
