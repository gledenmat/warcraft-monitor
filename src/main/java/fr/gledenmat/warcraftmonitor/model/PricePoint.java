package fr.gledenmat.warcraftmonitor.model;

import java.time.Instant;

/**
 * Représente un point sur la courbe du graphique.
 * Indépendant de l'API Blizzard (propre à notre app).
 */
public record PricePoint(
    Instant timestamp, // L'heure exacte du relevé
    long goldPrice     // Le prix en PO (déjà converti)
) {}