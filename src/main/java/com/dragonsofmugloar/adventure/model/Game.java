package com.dragonsofmugloar.adventure.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private  final String gameId;
    private int lives;
    private int gold;
    private int score;
    private int highScore;
    private int turn;
    private final List<String> messages = new ArrayList<>();

    public Game(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setScore(int points) {
        this.score = points;
    }

    public int getScore() {
        return score;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return gold;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public void appendMessage(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}