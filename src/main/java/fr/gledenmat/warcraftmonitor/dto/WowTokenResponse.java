package fr.gledenmat.warcraftmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO représentant la réponse de l'API Blizzard.
 * @JsonIgnoreProperties(ignoreUnknown = true) permet d'éviter les crashs
 * si l'API change et ajoute des champs inconnus.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // <--- AJOUT CRUCIAL
public record WowTokenResponse(
    @JsonProperty("last_updated_timestamp") long lastUpdatedTimestamp,
    @JsonProperty("price") long price
    // J'ai supprimé le champ "val" qui causait l'erreur
) {
    public long getGoldPrice() {
        return price / 10000;
    }
}