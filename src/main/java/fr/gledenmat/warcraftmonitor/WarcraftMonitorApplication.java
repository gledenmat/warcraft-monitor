package fr.gledenmat.warcraftmonitor;

import fr.gledenmat.warcraftmonitor.config.BlizzardConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(BlizzardConfig.class)
public class WarcraftMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarcraftMonitorApplication.class, args);
    }
}