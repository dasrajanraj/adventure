package com.dragonsofmugloar.adventure.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.dto.StartResponse;
import com.dragonsofmugloar.adventure.dto.TaskAttemptResponse;
import com.dragonsofmugloar.adventure.dto.TaskResponse;
import com.dragonsofmugloar.adventure.model.Game;
import com.dragonsofmugloar.adventure.util.Stats;
import com.dragonsofmugloar.adventure.util.TaskDecoder;

public class GameService {
    private static final Logger log = LogManager.getLogger(GameService.class);

    private final GameApi gameApiClient;

    // Temporary declaration for probability stats
    Map<String, Stats> probabilityStats = new java.util.HashMap<>() {{}};

    public GameService( GameApi gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public void startGames(int numOfGames) {        
        ArrayList<Game> games = initializeGames(numOfGames);

        if (games.size() != numOfGames) {
            log.warn("Only " + games.size() + " out of " + numOfGames + " games started by server.");
        }

        playGames(games);

        endGame();
    }

    private ArrayList<Game> initializeGames(int numberOfGames) {
        ArrayList<Game> games = new ArrayList<>();
        for (int i = 0; i < numberOfGames; i++) {
            StartResponse startResponse ;

            try {
                startResponse = gameApiClient.startGame();
            } catch (Exception e) {
                log.error("Error starting game " + (i + 1) + ": " + e.getMessage());
                continue;
            }

            var game = initializeGame(startResponse);

            games.add(game);
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

    private void playGames(ArrayList<Game> games) {
        var count = 0;

        for (Game game : games) {
            log.info("Starting game with ID: " + game.getGameId());
            playGame(game);
            if (game.getScore() >= 1000) {
                count++;
            }
            log.info("Game with ID: " + game.getGameId() + " ended. Score: " + game.getScore() + ", Lives left: " + game.getLives());
        }

        log.info("Total games won: " + count + " out of " + games.size());
    }

    private void playGame(Game game) {
        while (game.getLives() > 0 && game.getScore() < 1001) {
            List<TaskResponse> tasks;

            try {
                tasks = gameApiClient.getTasks(game.getGameId());
            } catch (Exception e) {
                log.error("Error fetching tasks for game " + game.getGameId() + ": " + e.getMessage());
                break;
            }

            if (tasks.isEmpty()) {
                log.error("No tasks available for game with ID: " + game.getGameId());
                break;
            }

            TaskResponse task = taskToSolve(tasks);
            if (task == null) {
                log.error("No suitable task found for game with ID: " + game.getGameId());
                break;
            }

            TaskAttemptResponse result;
            try {
                result = this.gameApiClient.attemptTask(game.getGameId(), task.getAdId());                    
            } catch (Exception e) {
                log.error("Error attempting task " + task.getAdId() + " for game " + game.getGameId() + ": " + e.getMessage());
                continue;   
            }

            probabilityStats
                .computeIfAbsent(task.getProbability(), p -> new Stats(0,0))
                .record(result.isSuccess());

            log.info("Task attempt result: " + (result.isSuccess() ? "Success" : "Failure") + 
                        ", Score: " + result.getScore() + 
                        ", Lives left: " + result.getLives() + 
                        ", Gold: " + result.getGold() + 
                        ", High Score: " + result.getHighScore() + 
                        ", Turn: " + result.getTurn() +
                        ", Message: " + result.getMessage() +
                        ", task: " + task.toString());
            updateGameState(game, result);
            buyItemsIfNeeded(game);
        }
    }

    private void updateGameState(Game game, TaskAttemptResponse result) {
        game.setScore(result.getScore());
        game.setLives(result.getLives());
        game.setGold(result.getGold());
        game.setHighScore(result.getHighScore());
        game.setTurn(result.getTurn());

        String message = result.getMessage();
        if (message != null && !message.isEmpty()) {
            game.appendMessage(message);
        }
    }

    private void buyItemsIfNeeded(Game game) {
        if (game.getGold() >= 100 && game.getLives() < 3) {
            try {
                var buyResponse = gameApiClient.getAvailableItems(game.getGameId());
                if (buyResponse.length == 0) {
                    log.info("No items available for purchase in game " + game.getGameId());
                    return;
                }

                // buy item with id "hpot" if available
                if (java.util.Arrays.stream(buyResponse).noneMatch(item -> "hpot".equals(item.getId()))) {
                    log.info("No health potion available for purchase in game " + game.getGameId());
                    return;
                }

                gameApiClient.makePurchase(game.getGameId(), "hpot");

            } catch (Exception e) {
                log.error("Error buying item for game " + game.getGameId() + ": " + e.getMessage());
            }
        }
    }

    private TaskResponse taskToSolve(List<TaskResponse> tasks) {
        return tasks.stream()
                .map(task -> {
                    if (task.getEncrypted() != null) {
                        return TaskDecoder.decodeTaskResponse(task);
                    }

                    return task;
                })
                .filter(task -> !task.getMessage().startsWith("Steal super awesome diamond"))
                .sorted(Comparator
                        .comparing(this::probabilityRank)
                        .thenComparing(TaskResponse::getReward, Comparator.reverseOrder()))
                .findFirst()
                .orElse(null);
    }

    private int probabilityRank(TaskResponse task) {
        return switch (task.getProbability().toLowerCase()) {
            case "piece of cake" -> 1;
            case "walk in the park" -> 2;
            case "sure thing" -> 3;
            case "quite likely" -> 4;
            case "rather detrimental" -> 5;
            case "hmmm...." -> 6;
            case "playing with fire" -> 7;
            case "risky" -> 8;
            case "gamble" -> 9;
            case "suicide mission" -> 10;
            case "impossible" -> 11;
            default -> {
                log.warn(task.toString());
                yield 12;
            }
        };
    }

    private void endGame() {
        log.info("Ending the game. Thank you for playing!");

        log.info("\n=== Probability Performance Stats ===");
        for (var entry : probabilityStats.entrySet()) {
            log.info(entry.getKey() + " => " + entry.getValue());
        }
    }
}
