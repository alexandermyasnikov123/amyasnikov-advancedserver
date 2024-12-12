package net.dunice.features.news.constants;

public interface KafkaConstants {
    String DELETION_TOPIC = "deletion-topic";

    String DELETION_GROUP = "deletion-group";

    String IMAGE_DELETION_TOPIC = "image-deletion-topic";

    int PARTITIONS = 8;

    int REPLICATION_FACTOR = 1;
}
