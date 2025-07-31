package com.dragonsofmugloar.adventure.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.dto.TaskAttemptResponse;
import com.dragonsofmugloar.adventure.dto.TaskResponse;
import com.dragonsofmugloar.adventure.model.Game;

public class GameRunner extends  Thread {
    
    private static final Logger log = LogManager.getLogger(GameRunner.class);

    private int MAX_ATTEMPTS_ALLOWED = 100;

    private final Game game;
    private final GameApi gameApiClient;
    private final TaskSelector taskSelector;
    private final ItemBuyer itemBuyer;

    public GameRunner(Game game, GameApi client) {
        this.game = game;
        this.gameApiClient = client;
        this.taskSelector = new TaskSelector();
        this.itemBuyer = new ItemBuyer(client);
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
        log.info("Playing game: " + game.getGameId());
        int attemptCount = 0;

        while (game.getLives() > 0 && game.getScore() < 1001  && attemptCount < MAX_ATTEMPTS_ALLOWED) {
            try {
                List<TaskResponse> tasks = gameApiClient.getTasks(game.getGameId());
                TaskResponse task = taskSelector.selectTask(tasks);

                if (task == null) {
                    log.warn("No valid task found for game: " + game.getGameId());
                    break;
                }

                TaskAttemptResponse result = gameApiClient.attemptTask(game.getGameId(), task.getAdId());

                log.info("Task attempt result: " + (result.isSuccess() ? "Success" : "Failure") + 
                    ", Score: " + result.getScore() + 
                    ", Lives left: " + result.getLives() + 
                    ", Gold: " + result.getGold() + 
                    ", High Score: " + result.getHighScore() + 
                    ", Turn: " + result.getTurn() +
                    ", Message: " + result.getMessage() +
                    ", task: " + task.toString());

                updateGameState(result);
                itemBuyer.buyIfNeeded(game);

            } catch (Exception e) {
                log.error("Error in game "+ game.getGameId() + ": " + e.getMessage());
                break;
            }

            attemptCount++;
        }

        log.info("Game " + game.getGameId() +" ended. Score: " + game.getScore() +", Lives: " + game.getLives());
    }

    private void updateGameState(TaskAttemptResponse result) {
        game.setScore(result.getScore());
        game.setLives(result.getLives());
        game.setGold(result.getGold());
        game.setHighScore(result.getHighScore());
        game.setTurn(result.getTurn());

        if (result.getMessage() != null) {
            game.appendMessage(result.getMessage());
        }
    }
}

