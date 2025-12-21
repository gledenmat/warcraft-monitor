package fr.gledenmat.warcraftmonitor.service;

import fr.gledenmat.warcraftmonitor.dto.WowTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import fr.gledenmat.warcraftmonitor.config.BlizzardConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@EnableConfigurationProperties(BlizzardConfig.class)
public class BlizzardService {

    private final RestClient restClient;

    private final BlizzardConfig config;

    public BlizzardService(RestClient.Builder builder, BlizzardConfig config) {
            this.restClient = builder.build();
            this.config = config;
    }

 // Étape 1 : S'authentifier (OAuth2 Client Credentials)
    private String getAccessToken() {
        log.debug("🔑 Récupération d'un nouveau Token OAuth Blizzard...");
        
        // On utilise l'objet 'config' injecté
        String auth = config.clientId() + ":" + config.clientSecret();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");

        // Utilisation de ParameterizedTypeReference pour éviter le warning
        Map<String, Object> response = restClient.post()
                .uri(config.authUrl()) // Utilisation de config.authUrl()
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

    // Étape 2 : Récupérer le prix avec le Token
    public long fetchTokenPrice() {
        try {
            String accessToken = getAccessToken();

            WowTokenResponse response = restClient.get()
                    .uri(config.apiUrl()) // Utilisation de config.apiUrl()
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(WowTokenResponse.class);

            if (response != null) {
                log.info("💰 PRIX DU TOKEN : {} PO", response.getGoldPrice());
                return response.getGoldPrice();
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'appel Blizzard : {}", e.getMessage(), e);
        }
        return -1;
    }
}