package fr.gledenmat.warcraftmonitor.repository;

import fr.gledenmat.warcraftmonitor.model.PricePoint;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryPriceRepository {

    // On garde l'historique dans une liste Thread-Safe
    // (car le Scheduler écrit dedans pendant que le Front lit dedans)
    private final List<PricePoint> history = new CopyOnWriteArrayList<>();

    // Limite glissante : On garde par exemple les 1000 derniers points
    // Avec 1 point toutes les 20 min, 1000 points = ~14 jours d'historique.
    private static final int MAX_HISTORY_SIZE = 1000;

    public void addPrice(PricePoint point) {
        history.add(point);
        
        // Nettoyage automatique des vieux points si on dépasse la limite
        if (history.size() > MAX_HISTORY_SIZE) {
            history.remove(0); // On supprime le plus vieux
        }
    }

    public List<PricePoint> getAllHistory() {
        // On retourne une copie pour éviter que quelqu'un modifie la liste originale
        return new ArrayList<>(history);
    }
    
    public PricePoint getLastPrice() {
        if (history.isEmpty()) return null;
        return history.get(history.size() - 1);
    }
}