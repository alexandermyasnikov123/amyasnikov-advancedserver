package net.dunice.features.auth.configurations;

import lombok.RequiredArgsConstructor;
import net.dunice.features.auth.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private final String bootstrapAddress;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    @Bean
    public NewTopic deletionTopic() {
        return TopicBuilder
                .name(KafkaConstants.DELETION_TOPIC)
                .partitions(KafkaConstants.PARTITIONS)
                .replicas(KafkaConstants.REPLICATION_FACTOR)
                .build();
    }
}
