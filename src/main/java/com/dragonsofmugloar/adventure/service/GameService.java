package com.dragonsofmugloar.adventure.service;

import java.util.ArrayList;
import java.util.List;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.dto.StartResponse;
import com.dragonsofmugloar.adventure.dto.TaskAttemptResponse;
import com.dragonsofmugloar.adventure.dto.TaskResponse;
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

                if (result.isSuccess()) {
                    game.setScore(result.getScore());
                } else{
                    game.setLives(result.getLives());
                }
            }
        }
    }

    private TaskResponse taskToSolve(List<TaskResponse> tasks) {
        TaskResponse task = tasks.get(0);
        return task;
    }

    private void endGame() {
        System.out.println("Ending the game. Thank you for playing!");
    }
}
