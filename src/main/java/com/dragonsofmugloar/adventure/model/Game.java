package com.dragonsofmugloar.adventure.model;

public class Game {

    private String gameId;
    private int score;
    private int lives;

    public Game(String gameId, int score, int lives) {
        this.gameId = gameId;
        this.score = score;
        this.lives = lives;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
    
}
