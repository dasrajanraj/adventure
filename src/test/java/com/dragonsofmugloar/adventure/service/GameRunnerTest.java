package com.dragonsofmugloar.adventure.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.dto.AvailableItemResponse;
import com.dragonsofmugloar.adventure.dto.TaskAttemptResponse;
import com.dragonsofmugloar.adventure.dto.TaskResponse;
import com.dragonsofmugloar.adventure.model.Game;

class GameRunnerTest {

    private GameApi api;
    private Game game;

    @BeforeEach
    void setUp() {
        api = mock(GameApi.class);
        game = new Game("test-game");
        game.setLives(1);
        game.setScore(0);
    }

    @Test
    void shouldRunGameUntilLivesEndOrScoreThreshold() throws Exception {
        TaskResponse task = new TaskResponse();
        task.setAdId("ad1");
        task.setProbability("sure thing");
        task.setReward(1001);
        task.setMessage("Complete the quest");

        TaskAttemptResponse result = new TaskAttemptResponse();
        result.setSuccess(true);
        result.setScore(1001);
        result.setLives(0);
        result.setGold(0);
        result.setHighScore(0);
        result.setTurn(2);
        result.setMessage("You succeeded");

        when(api.getTasks("test-game")).thenReturn(new ArrayList<>(List.of(task)));
        when(api.attemptTask("test-game", "ad1")).thenReturn(result);
        when(api.getAvailableItems("test-game")).thenReturn(new AvailableItemResponse[0]);

        GameRunner runner = new GameRunner(game, api);
        runner.run();

        assertEquals(1001, game.getScore());
        assertEquals(0, game.getLives());
    }

    @Test
    void shouldHandleNoTasksAvailable() throws Exception {
        when(api.getTasks("test-game")).thenReturn(new ArrayList<>());

        GameRunner runner = new GameRunner(game, api);
        runner.run();

        assertEquals(0, game.getScore());
    }
    
    @Test
    void shouldHandleTaskFailure() throws Exception {
        TaskResponse task = new TaskResponse();
        task.setAdId("ad1");
        task.setProbability("sure thing");
        task.setReward(1001);
        task.setMessage("Complete the quest");

        TaskAttemptResponse result = new TaskAttemptResponse();
        result.setSuccess(false);
        result.setScore(0);
        result.setLives(1);
        result.setGold(0);
        result.setHighScore(0);
        result.setTurn(1);
        result.setMessage("You failed");

        when(api.getTasks("test-game")).thenReturn(new ArrayList<>(List.of(task)));
        when(api.attemptTask("test-game", "ad1")).thenReturn(result);
        when(api.getAvailableItems("test-game")).thenReturn(new AvailableItemResponse[0]);

        GameRunner runner = new GameRunner(game, api);
        runner.run();

        assertEquals(0, game.getScore());
    }
}
