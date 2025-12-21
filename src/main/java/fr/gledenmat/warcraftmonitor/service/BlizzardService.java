package fr.gledenmat.warcraftmonitor.service;

import fr.gledenmat.warcraftmonitor.config.BlizzardConfig;
import fr.gledenmat.warcraftmonitor.dto.WowTokenResponse;
import lombok.extern.slf4j.Slf4j; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate; 
import org.springframework.scheduling.annotation.Scheduled; 
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Map;

@Slf4j 
@Service
@EnableConfigurationProperties(BlizzardConfig.class)
public class BlizzardService {

    private final RestClient restClient;
    private final BlizzardConfig config;
    
    // --- KAFKA ---
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    // Constructeur avec injection de Kafka et du nom du topic
    public BlizzardService(RestClient.Builder builder, 
                           BlizzardConfig config,
                           KafkaTemplate<String, String> kafkaTemplate,
                           @Value("${warcraft.kafka.topic-name}") String topicName) {
        this.restClient = builder.build();
        this.config = config;
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    private String getAccessToken() {
        log.debug("🔑 Récupération d'un nouveau Token OAuth Blizzard...");
        
        // --- SECURITÉ : .trim() pour nettoyer les espaces du fichier de launch ---
        String auth = config.clientId().trim() + ":" + config.clientSecret().trim();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");

        Map<String, Object> response = restClient.post()
                .uri(config.authUrl())
                .header("Authorization", "Basic " + encodedAuth)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Impossible de récupérer le token d'accès Blizzard");
        }

        return (String) response.get("access_token");
    }

    // --- AUTOMATISATION : Scheduler ---
    @Scheduled(fixedRateString = "${warcraft.check-interval:1200000}", initialDelay = 5000)
    public long fetchTokenPrice() {
        try {
            String accessToken = getAccessToken();

            WowTokenResponse response = restClient.get()
                    .uri(config.apiUrl())
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(WowTokenResponse.class);

            if (response != null) {
                long price = response.getGoldPrice();
                log.info("💰 PRIX DU TOKEN : {} PO", price);
                
                // --- ENVOI KAFKA ---
                String message = String.valueOf(price);
                kafkaTemplate.send(topicName, message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("✅ Message envoyé à Kafka sur le VPS : {}", message); 
                        } else {
                            log.error("❌ Échec de l'envoi Kafka : {}", ex.getMessage());
                        }
                    });

                return price;
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'appel Blizzard : {}", e.getMessage(), e);
        }
        return -1;
    }
}