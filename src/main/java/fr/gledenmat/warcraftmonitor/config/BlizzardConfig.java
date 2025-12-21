package fr.gledenmat.warcraftmonitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Java 25 Record : concis, immuable, parfait pour la config
@ConfigurationProperties(prefix = "blizzard")
public record BlizzardConfig(
    String clientId,
    String clientSecret,
    String authUrl,
    String apiUrl
) {}
