package fr.gledenmat.warcraftmonitor.controller;

import fr.gledenmat.warcraftmonitor.model.PricePoint;
import fr.gledenmat.warcraftmonitor.repository.InMemoryPriceRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Autorise Angular
public class PriceController {

    private final InMemoryPriceRepository priceRepository;

    // On injecte le repository (la mémoire) dans le contrôleur
    public PriceController(InMemoryPriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /**
     * Renvoie juste le dernier prix connu (pour l'affichage immédiat en haut du site)
     */
    @GetMapping("/api/last-price")
    public long getLastPrice() {
        PricePoint last = priceRepository.getLastPrice();
        if (last != null) {
            return last.goldPrice();
        }
        return 0; // Si l'historique est vide
    }

    /**
     * NOUVEAU : Renvoie tout l'historique pour dessiner le graphique
     */
    @GetMapping("/api/history")
    public List<PricePoint> getHistory() {
        return priceRepository.getAllHistory();
    }
}