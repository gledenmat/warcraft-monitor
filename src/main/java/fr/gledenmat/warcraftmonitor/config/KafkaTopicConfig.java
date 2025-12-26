package fr.gledenmat.warcraftmonitor.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// Configuration pour la création automatique du topic Kafka
@Configuration
public class KafkaTopicConfig {

    @Value("${warcraft.kafka.topic-name}")
    private String topicName;

    @Bean
    public NewTopic warcraftTokenTopic() {
        // On crée le topic automatiquement s'il n'existe pas
        return TopicBuilder.name(topicName)
                .partitions(3) // Standard pour commencer
                .replicas(1)   // 1 seul réplica car 1 seul noeud Kafka sur le VPS
                .build();
    }
}