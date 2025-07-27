package com.dragonsofmugloar.adventure;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.client.GameApiClient;
import com.dragonsofmugloar.adventure.service.GameService;


@SpringBootApplication
public class AdventureApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AdventureApplication.class, args);
    }

	@Override
    public void run(String... args) {
        System.out.println("How many Dragon of Mugloar games do you want to start?");

        try (Scanner scanner = new Scanner(System.in)) {
			if (scanner.hasNextInt()) {
                int numberOfGames = scanner.nextInt();
                System.out.println("You have chosen to play " + numberOfGames + " games today!");

                GameApi gameApiClient = new GameApiClient();
                GameService gameService = new GameService(gameApiClient);

                gameService.startGames(numberOfGames);
            } else {
                System.out.println("Invalid input. Please enter a number.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
