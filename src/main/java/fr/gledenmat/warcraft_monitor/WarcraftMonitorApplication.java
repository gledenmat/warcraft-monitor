package fr.gledenmat.warcraft_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController // <--- AJOUT 1 : Transforme ça en site web
public class WarcraftMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarcraftMonitorApplication.class, args);
    }

    // <--- AJOUT 2 : La page d'accueil
    @GetMapping("/")
    public String test() {
        return "<h1>✅ Victoire !</h1><p>Ton environnement Java 25 fonctionne sur VS Code.</p>";
    }
}