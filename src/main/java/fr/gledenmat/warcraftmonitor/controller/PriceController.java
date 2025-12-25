package fr.gledenmat.warcraftmonitor.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") // Important pour qu'Angular puisse discuter avec Java
public class PriceController {

    // Notre mémoire tampon (accessible de partout)
    public static int LAST_KNOWN_PRICE = 0;

    @GetMapping("/api/last-price")
    public Integer getLastPrice() {
        return LAST_KNOWN_PRICE;
    }
}