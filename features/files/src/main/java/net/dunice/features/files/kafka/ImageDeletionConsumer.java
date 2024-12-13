package net.dunice.features.files.kafka;

import lombok.RequiredArgsConstructor;
import net.dunice.features.files.constants.KafkaConstants;
import net.dunice.features.files.services.FilesService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageDeletionConsumer {
    private final FilesService filesService;

    @KafkaListener(topics = KafkaConstants.IMAGE_DELETION_TOPIC, groupId = KafkaConstants.IMAGE_DELETION_GROUP)
    public void onImagesDeletion(List<String> imageUrls) {
        imageUrls.forEach(filesService::deleteFileByUrl);
    }
}
