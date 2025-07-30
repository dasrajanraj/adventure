package com.dragonsofmugloar.adventure.service;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dragonsofmugloar.adventure.AdventureApplication;
import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.model.Game;

public class ItemBuyer {

    private static final Logger log = LogManager.getLogger(AdventureApplication.class);
    private final GameApi gameApiClient;

    public ItemBuyer(GameApi gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public void buyIfNeeded(Game game) {
        if (game.getGold() >= 100 && game.getLives() < 3) {
            try {
                var items = gameApiClient.getAvailableItems(game.getGameId());

                boolean hasHealthPotion = Arrays.stream(items).anyMatch(i -> "hpot".equals(i.getId()));
                if (hasHealthPotion) {
                    gameApiClient.makePurchase(game.getGameId(), "hpot");
                }
            } catch (Exception e) {
                log.error("Error buying item: " + e.getMessage());
            }
        }
    }
}
