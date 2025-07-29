package com.dragonsofmugloar.adventure.util;

import com.dragonsofmugloar.adventure.dto.TaskResponse;

public class TaskDecoder {
    
    public static TaskResponse decodeTaskResponse(TaskResponse taskResponse) {
        String encryption = taskResponse.getEncrypted();

        if (encryption.equals("1")) {
            taskResponse.setProbability(Decoder.decodeBase64(taskResponse.getProbability()));
            taskResponse.setAdId(Decoder.decodeBase64(taskResponse.getAdId()));
            taskResponse.setMessage(Decoder.decodeBase64(taskResponse.getMessage()));
        }

        return taskResponse;
    }
}
