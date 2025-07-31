package com.dragonsofmugloar.adventure;

import java.util.Scanner;

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
        System.out.println("How many Dragon of Mugloar games do you want to start?");

        try (Scanner scanner = new Scanner(System.in)) {
            if (scanner.hasNextInt()) {
                int numberOfGames = scanner.nextInt();

                GameApi gameApiClient = new GameApiClient();
                GameService gameService = new GameService(gameApiClient);

                gameService.startGames(numberOfGames);
            } else {
                log.error("Invalid input. Please enter a number.");
            }
        } catch (Exception e) {
            log.error("An error occurred while starting the game: " + e.getMessage(), e);
        } finally {
            log.info("Thank you for playing! Goodbye!");
        }
    }
}
