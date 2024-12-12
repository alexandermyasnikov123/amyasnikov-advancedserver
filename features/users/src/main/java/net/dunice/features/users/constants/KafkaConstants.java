package net.dunice.features.users.constants;

public interface KafkaConstants {
    String DELETION_TOPIC = "deletion-topic";

    String IMAGE_DELETION_TOPIC = "image-deletion-topic";

    String DELETION_GROUP = "deletion-group";

    int PARTITIONS = 8;

    int REPLICATION_FACTOR = 1;
}
