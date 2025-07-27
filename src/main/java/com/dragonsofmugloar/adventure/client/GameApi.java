package com.dragonsofmugloar.adventure.client;

import java.util.ArrayList;

import com.dragonsofmugloar.adventure.dto.AvailableItemResponse;
import com.dragonsofmugloar.adventure.dto.PurchaseResponse;
import com.dragonsofmugloar.adventure.dto.ReputationResponse;
import com.dragonsofmugloar.adventure.dto.StartResponse;
import com.dragonsofmugloar.adventure.dto.TaskAttemptResponse;
import com.dragonsofmugloar.adventure.dto.TaskResponse;

public interface GameApi {
 
    public StartResponse startGame();

    public ArrayList<TaskResponse> getTasks(String gameId);

    public TaskAttemptResponse attemptTask(String gameId, String taskId);

    public PurchaseResponse makePurchase(String gameId, String itemId);

    public ReputationResponse checkReputation(String gameId);  
    
    public AvailableItemResponse[] getAvailableItems(String gameId);
}
