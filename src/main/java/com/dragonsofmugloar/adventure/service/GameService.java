package com.dragonsofmugloar.adventure.service;

import java.util.ArrayList;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.dto.StartResponse;
import com.dragonsofmugloar.adventure.model.Game;

public class GameService {
    private final GameApi gameApiClient;

    public GameService( GameApi gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public void startGames(int numOfGames) {        
        ArrayList<Game> games = initializeGames(numOfGames);

        if (games.size() != numOfGames) {
            System.out.println("Only " + games.size() + " out of " + numOfGames + " games started by server.");
        }

        // playGames(games);

        // endGame();
    }

    private ArrayList<Game> initializeGames(int numberOfGames) {
        ArrayList<Game> games = new ArrayList<>();
        for (int i = 0; i < numberOfGames; i++) {
            StartResponse startResponse ;

            try {
                startResponse = gameApiClient.startGame();
            } catch (Exception e) {
                System.out.println("Error starting game " + (i + 1) + ": " + e.getMessage());
                continue;
            }

            Game game = new Game(
                startResponse.getGameId(), 
                startResponse.getScore(), 
                startResponse.getLives()
            );
            games.add(game);
        }
        return games;
    }
}
