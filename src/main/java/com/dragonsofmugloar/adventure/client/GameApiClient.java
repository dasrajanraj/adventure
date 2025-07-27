package com.dragonsofmugloar.adventure.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import com.dragonsofmugloar.adventure.constants.Constants;
import com.dragonsofmugloar.adventure.dto.AvailableItemResponse;
import com.dragonsofmugloar.adventure.dto.PurchaseResponse;
import com.dragonsofmugloar.adventure.dto.ReputationResponse;
import com.dragonsofmugloar.adventure.dto.StartResponse;
import com.dragonsofmugloar.adventure.dto.TaskAttemptResponse;
import com.dragonsofmugloar.adventure.dto.TaskResponse;
import com.google.gson.Gson;

public class GameApiClient implements GameApi {

    private final Gson gson = new Gson();

    @Override
    public StartResponse startGame() {
        String urlStr = Constants.GAME_BASE_URL + "/game/start";
        return makeRequest(urlStr, "POST", StartResponse.class, "Failed to start game");
    }

    @Override
    public ArrayList<TaskResponse> getTasks(String gameId) {
        String urlStr = Constants.GAME_BASE_URL + gameId + "/messages";
        TaskResponse[] tasks = makeRequest(urlStr, "GET", TaskResponse[].class, "Failed to get tasks");
        return new ArrayList<>(Arrays.asList(tasks));
    }

    @Override
    public TaskAttemptResponse attemptTask(String gameId, String taskId) {
        String urlStr = Constants.GAME_BASE_URL + gameId + "/solve/" + taskId;
        return makeRequest(urlStr, "POST", TaskAttemptResponse.class, "Failed to attempt task");
    }

    @Override
    public PurchaseResponse makePurchase(String gameId, String itemId) {
        String urlStr = Constants.GAME_BASE_URL + "/shop/buy/" + itemId;
        return makeRequest(urlStr, "POST", PurchaseResponse.class, "Failed to make purchase");
    }

    @Override
    public ReputationResponse checkReputation(String gameId) {
        String urlStr = Constants.GAME_BASE_URL + gameId + "/investigate/reputation";
        return makeRequest(urlStr, "GET", ReputationResponse.class, "Failed to check reputation");
    }

    @Override
    public AvailableItemResponse[] getAvailableItems(String gameId) {
        String urlStr = Constants.GAME_BASE_URL + gameId + "/shop";
        return makeRequest(urlStr, "GET", AvailableItemResponse[].class, "Failed to get available items");
    }

    private <T> T makeRequest(String urlStr, String method, Class<T> responseType, String errorMessage) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            if ("POST".equalsIgnoreCase(method)) {
                conn.setDoOutput(true);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream is = conn.getInputStream();
                     Reader reader = new InputStreamReader(is)) {
                    return gson.fromJson(reader, responseType);
                }
            } else {
                throw new RuntimeException(errorMessage + ": HTTP " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(errorMessage + ": " + e.getMessage(), e);
        }
    }
}
