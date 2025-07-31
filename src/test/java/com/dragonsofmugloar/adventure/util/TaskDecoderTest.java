package com.dragonsofmugloar.adventure.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.dragonsofmugloar.adventure.dto.TaskResponse;

class TaskDecoderTest {

    @Test
    void decodeTaskResponse_shouldDecodeBase64EncodedTask() {
        TaskResponse task = new TaskResponse();
        task.setEncrypted("1");
        task.setProbability("U3VyZSBUaGluZw=="); // "Sure Thing"
        task.setAdId("YWQxMjM="); // "ad123"
        task.setMessage("VGhpcyBpcyBhIHRlc3Q="); // "This is a test"

        TaskResponse decoded = TaskDecoder.decodeTaskResponse(task);

        assertEquals("Sure Thing", decoded.getProbability());
        assertEquals("ad123", decoded.getAdId());
        assertEquals("This is a test", decoded.getMessage());
    }

    @Test
    void decodeTaskResponse_shouldDecodeRot13EncodedTask() {
        TaskResponse task = new TaskResponse();
        task.setEncrypted("2");
        task.setProbability("Fher Guvat"); // "Sure Thing"
        task.setAdId("nq123"); // "ad123"
        task.setMessage("Guvf vf n grfg"); // "This is a test"

        TaskResponse decoded = TaskDecoder.decodeTaskResponse(task);

        assertEquals("Sure Thing", decoded.getProbability());
        assertEquals("ad123", decoded.getAdId());
        assertEquals("This is a test", decoded.getMessage());
    }

    @Test
    void decodeTaskResponse_shouldHandleUnknownEncryption() {
        TaskResponse task = new TaskResponse();
        task.setEncrypted("unknown");
        task.setProbability("unchanged");
        task.setAdId("unchanged");
        task.setMessage("unchanged");

        TaskResponse decoded = TaskDecoder.decodeTaskResponse(task);

        assertEquals("unchanged", decoded.getProbability());
        assertEquals("unchanged", decoded.getAdId());
        assertEquals("unchanged", decoded.getMessage());
    }
}
