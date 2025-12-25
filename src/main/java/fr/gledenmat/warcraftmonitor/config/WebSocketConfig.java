package fr.gledenmat.warcraftmonitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // On active un broker simple en mémoire pour dispatcher les messages
        // Les clients (Angular) s'abonneront aux URLs commençant par "/topic"
        config.enableSimpleBroker("/topic");
        
        // Préfixe pour les messages envoyés DU client VERS le serveur (pas utilisé ici pour l'instant)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // On définit le point d'entrée WebSocket
        registry.addEndpoint("/ws")
                // IMPORTANT : On autorise Angular (port 4200) à se connecter
                .setAllowedOriginPatterns("*");
                // .withSockJS();  <-- ON A SUPPRIMÉ CETTE LIGNE !
    }
}