package com.dragonsofmugloar.adventure.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dragonsofmugloar.adventure.client.GameApi;
import com.dragonsofmugloar.adventure.model.Game;
import com.dragonsofmugloar.adventure.dto.AvailableItemResponse;


class ItemBuyerTest {

    private GameApi api;
    private ItemBuyer buyer;
    private Game game;

    @BeforeEach
    void setUp() {
        api = mock(GameApi.class);
        buyer = new ItemBuyer(api);
        game = new Game("g123");
        game.setGold(150);
        game.setLives(2);
    }

    @Test
    void shouldBuyHealthPotionIfAvailable() throws Exception {
        var item = new AvailableItemResponse();
        item.setId("hpot");

        when(api.getAvailableItems("g123")).thenReturn(new AvailableItemResponse[]{item});

        buyer.buyIfNeeded(game);

        verify(api).makePurchase("g123", "hpot");
    }

    @Test
    void shouldNotBuyIfPotionNotAvailable() throws Exception {
        var item = new AvailableItemResponse();
        item.setId("xpot");

        when(api.getAvailableItems("g123")).thenReturn(new AvailableItemResponse[]{item});

        buyer.buyIfNeeded(game);

        verify(api, never()).makePurchase(anyString(), anyString());
    }
}

