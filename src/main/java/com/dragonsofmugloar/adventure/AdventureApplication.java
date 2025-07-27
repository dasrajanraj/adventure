package com.dragonsofmugloar.adventure;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
                int numberOfDragons = scanner.nextInt();
                System.out.println("You have chosen to play " + numberOfDragons + " games today!");
            } else {
                System.out.println("Invalid input. Please enter a number.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
