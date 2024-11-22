package net.dunice.newsapi.entities.callbacks;

import net.dunice.newsapi.entities.NewsEntity;
import net.dunice.newsapi.services.FilesService;
import org.springframework.stereotype.Component;

@Component
public class NewsEntityCallbacks extends BaseImageProviderCallbacks<NewsEntity> {

    public NewsEntityCallbacks(FilesService service) {
        super(service);
    }
}
