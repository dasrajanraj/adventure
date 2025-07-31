package com.dragonsofmugloar.adventure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.client.GameApiClient;
import com.dragonsofmugloar.adventure.service.GameService;


@SpringBootApplication
public class AdventureApplication implements CommandLineRunner {

    private static final Logger log = LogManager.getLogger(AdventureApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AdventureApplication.class, args);
    }

    @Override
    public void run(String... args) {
        int numberOfGames = getNumberOfGames(args);

        if (numberOfGames <= 0) {
            log.error("Invalid number of games. Exiting.");
            return;
        }

        try {            
            GameApi gameApi = new GameApiClient();
            GameService gameService = new GameService(gameApi);
            gameService.startGames(numberOfGames);
        } catch (Exception e) {
            log.error("An error occurred while starting games: {}", e.getMessage(), e);
        }

        log.info("Thank you for playing! Goodbye!");
    }

    private int getNumberOfGames(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                log.warn("Invalid number format in arguments: {}", args[0]);
            }
        }

        return -1;
    }
}
