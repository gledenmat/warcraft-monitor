package fr.gledenmat.warcraftmonitor;

import fr.gledenmat.warcraftmonitor.service.BlizzardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class WarcraftMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarcraftMonitorApplication.class, args);
    }

    /**
     * Ce Bean s'exécute une seule fois au démarrage de l'application. 
     * Il teste la connexion au service Blizzard
     */
    @Bean
    CommandLineRunner run(BlizzardService blizzardService) {
        return args -> {
            log.info("--- 🚀 DÉMARRAGE DU TEST BLIZZARD ---");
            long price = blizzardService.fetchTokenPrice();
            
            if (price > 0) {
                log.info("--- ✅ TEST RÉUSSI : Connexion Blizzard opérationnelle ---");
            } else {
                log.error("--- ❌ TEST ÉCHOUÉ : Impossible de récupérer le prix ---");
            }
        };
    }
}