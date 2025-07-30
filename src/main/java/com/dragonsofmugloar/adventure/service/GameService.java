package com.dragonsofmugloar.adventure.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.dto.StartResponse;
import com.dragonsofmugloar.adventure.model.Game;

public class GameService {

    private static final Logger log = LogManager.getLogger(GameService.class);

    private final GameApi gameApiClient;

    public GameService(GameApi gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public void startGames(int numberOfGames) {
        List<Game> games = initializeGames(numberOfGames);

        log.info("You have chosen to play " + numberOfGames + " games today!");

        int wonGames = 0;
        for (Game game : games) {
            GameRunner runner = new GameRunner(game, gameApiClient);
            runner.play();

            if (game.getScore() >= 1000) {
                wonGames++;
            }
        }

        log.info("Total games won: " + wonGames + " out of " + games.size());
    }

    private List<Game> initializeGames(int count) {
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                StartResponse startResponse = gameApiClient.startGame();
                games.add(initializeGame(startResponse));
            } catch (Exception e) {
                log.error("Failed to start game #{}: {}", i + 1, e.getMessage());
            }
        }
        return games;
    }

    private Game initializeGame(StartResponse startResponse) {
        Game game = new Game(startResponse.getGameId());

        game.setLives(startResponse.getLives());
        game.setScore(startResponse.getScore());
        game.setGold(startResponse.getGold());
        game.setHighScore(startResponse.getHighScore());
        game.setTurn(startResponse.getTurn());

        return game;
    }

}
