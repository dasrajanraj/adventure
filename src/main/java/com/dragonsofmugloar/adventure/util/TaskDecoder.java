package com.dragonsofmugloar.adventure.util;

import com.dragonsofmugloar.adventure.dto.TaskResponse;

public class TaskDecoder {
    
    public static TaskResponse decodeTaskResponse(TaskResponse taskResponse) {
        String encryption = taskResponse.getEncrypted();

        switch (encryption) {
            case "1":
                taskResponse.setProbability(Decoder.decodeBase64(taskResponse.getProbability()));
                taskResponse.setAdId(Decoder.decodeBase64(taskResponse.getAdId()));
                taskResponse.setMessage(Decoder.decodeBase64(taskResponse.getMessage()));    
                break;
            case "2":
                taskResponse.setProbability(Decoder.decodeRot13(taskResponse.getProbability()));
                taskResponse.setAdId(Decoder.decodeRot13(taskResponse.getAdId()));
                taskResponse.setMessage(Decoder.decodeRot13(taskResponse.getMessage()));
            default:
                System.out.println("Unknown encryption type: " + encryption);
        }

        return taskResponse;
    }
}
