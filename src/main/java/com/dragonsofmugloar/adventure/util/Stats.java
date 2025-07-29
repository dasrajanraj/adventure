package com.dragonsofmugloar.adventure.util;

public class Stats {
    int tried = 0;
    int success = 0;

    public Stats(int tried, int success) {
        this.tried = tried;
        this.success = success;
    }

    public void record(boolean wasSuccess) {
        tried++;
        if (wasSuccess) success++;
    }

    @Override
    public String toString() {
        return "Tried: " + tried + ", Success: " + success +
               ", Success Rate: " + (tried > 0 ? (success * 100 / tried) + "%" : "0%");
    }
}
