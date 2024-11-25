package net.dunice.newsapi.entities.callbacks;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.entities.NewsEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NewsEntityCallbacks {
    private final ImageProviderHandler callbacks;

    @PostLoad
    public void postLoad(NewsEntity entity) {
        callbacks.cachePreviousState(entity);
    }

    @PostRemove
    public void postRemove(NewsEntity ignored) {
        callbacks.deleteCachedAvatar();
    }

    @PostUpdate
    public void postUpdate(NewsEntity currentState) {
        callbacks.deleteAvatarIfAbsent(currentState);
    }
}
