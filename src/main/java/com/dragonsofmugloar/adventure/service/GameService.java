package com.dragonsofmugloar.adventure.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.dto.StartResponse;
import com.dragonsofmugloar.adventure.dto.TaskAttemptResponse;
import com.dragonsofmugloar.adventure.dto.TaskResponse;
import com.dragonsofmugloar.adventure.model.Game;
import com.dragonsofmugloar.adventure.util.Stats;
import com.dragonsofmugloar.adventure.util.TaskDecoder;

public class GameService {
    private final GameApi gameApiClient;

    // Temporary declaration for probability stats
    Map<String, Stats> probabilityStats = new java.util.HashMap<>() {{}};

    public GameService( GameApi gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public void startGames(int numOfGames) {        
        ArrayList<Game> games = initializeGames(numOfGames);

        if (games.size() != numOfGames) {
            System.out.println("Only " + games.size() + " out of " + numOfGames + " games started by server.");
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

    private void playGames(ArrayList<Game> games) {
        var count = 0;

        for (Game game : games) {
            System.out.println("Playing game with ID: " + game.getGameId());
            playGame(game);
            if (game.getScore() >= 1000) {
                count++;
            }
            System.out.println("Game with ID: " + game.getGameId() + " ended. Score: " + game.getScore() + ", Lives left: " + game.getLives());
        }

        System.out.println("Total games won: " + count + " out of " + games.size());
    }

    private void playGame(Game game) {
        while (game.getLives() > 0 && game.getScore() < 1001) {
            List<TaskResponse> tasks;

            try {
                tasks = gameApiClient.getTasks(game.getGameId());
            } catch (Exception e) {
                System.out.println("Error fetching tasks for game " + game.getGameId() + ": " + e.getMessage());
                break;
            }

            if (tasks.isEmpty()) {
                System.out.println("No tasks available for game with ID: " + game.getGameId());
                break;
            }

            TaskResponse task = taskToSolve(tasks);
            System.out.println("Selected task: " + (task != null ? task.getAdId() : "None"));

            if (task != null) {
                TaskAttemptResponse result;
                try {
                    result = this.gameApiClient.attemptTask(game.getGameId(), task.getAdId());                    
                } catch (Exception e) {
                    System.out.println("Error attempting task " + task.getAdId() + " for game " + game.getGameId() + ": " + e.getMessage());
                    continue;   
                }

                probabilityStats
                    .computeIfAbsent(task.getProbability(), p -> new Stats(0,0))
                    .record(result.isSuccess());

                if (result.isSuccess()) {
                    game.setScore(result.getScore());
                } else{
                    game.setLives(result.getLives());
                }
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
            case "U3VpY2lkZSBtaXNzaW9u" -> 7;
            case "playing with fire" -> 8;
            case "risky" -> 9;
            case "gamble" -> 10;
            case "suicide mission" -> 11;
            case "impossible" -> 12;
            default -> {
                System.out.println(task.toString());
                yield 12;
            }
        };
    }

    private void endGame() {
        System.out.println("Ending the game. Thank you for playing!");

        System.out.println("\n=== Probability Performance Stats ===");
        for (var entry : probabilityStats.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}
