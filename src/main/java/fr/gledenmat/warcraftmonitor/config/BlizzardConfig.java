package fr.gledenmat.warcraftmonitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// ConfigurationProperties pour lier les propriétés de configuration avec le prefix "blizzard"
@ConfigurationProperties(prefix = "blizzard")
public record BlizzardConfig(
    String clientId,
    String clientSecret,
    String authUrl,
    String apiUrl
) {}
