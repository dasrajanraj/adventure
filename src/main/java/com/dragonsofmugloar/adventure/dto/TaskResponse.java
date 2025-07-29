package com.dragonsofmugloar.adventure.dto;

public class TaskResponse {
    private String adId;
    private String message;
    private int reward;
    private int expiresIn;
    private String encrypted;
    private String probability;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "TaskResponse{" +
                "adId='" + adId + '\'' +
                ", message='" + message + '\'' +
                ", reward=" + reward +
                ", expiresIn=" + expiresIn +
                ", encrypted='" + encrypted + '\'' +
                ", probability='" + probability + '\'' +
                '}';
    }
}
