package fr.gledenmat.warcraftmonitor.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import fr.gledenmat.warcraftmonitor.controller.PriceController;

@Service
@Slf4j
public class WarcraftTokenConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public WarcraftTokenConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${warcraft.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTokenPrice(String message) {
        log.info("📥 CONSUMER : Reçu de Kafka <{}>", message);

        // --- 1. MISE EN MÉMOIRE (Pour l'affichage immédiat au démarrage) ---
        try {
            // On convertit le texte "400000" en nombre réel
            int price = Integer.parseInt(message);
            // On le sauvegarde dans la "mémoire" du Controller
            PriceController.LAST_KNOWN_PRICE = price;
        } catch (NumberFormatException e) {
            log.warn("Impossible de lire le prix reçu : {}", message);
        }

        // --- 2. DIFFUSION WEBSOCKET (Pour le temps réel) ---
        // On pousse le message vers Angular
        messagingTemplate.convertAndSend("/topic/price", message);
        
        log.debug("📡 WEBSOCKET : Diffusé aux clients Angular");
    }
}