package com.dragonsofmugloar.adventure.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dragonsofmugloar.adventure.dto.TaskResponse;

public class TaskDecoder {
    private static final Logger log = LogManager.getLogger(TaskDecoder.class);

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
                break;
            default:
                log.warn("Unknown encryption type: " + encryption);
        }

        return taskResponse;
    }
}
