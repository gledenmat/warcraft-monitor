package fr.gledenmat.warcraftmonitor.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WarcraftTokenConsumer {
    // C'est le "Facteur WebSocket" qui permet d'envoyer des messages aux clients connectés
    private final SimpMessagingTemplate messagingTemplate;

    public WarcraftTokenConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Cette méthode est déclenchée automatiquement à chaque fois qu'un message
     * arrive dans le topic Kafka 'warcraft-token-price'.
     */
    @KafkaListener(topics = "${warcraft.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTokenPrice(String message) {
        log.info("📥 CONSUMER : Reçu de Kafka <{}>", message);

        // On pousse le message vers tous les clients Web connectés sur le canal "/topic/price"
        // Le Frontend Angular s'abonnera à ce canal.
        messagingTemplate.convertAndSend("/topic/price", message);
        
        log.debug("📡 WEBSOCKET : Diffusé aux clients Angular");
    }
}