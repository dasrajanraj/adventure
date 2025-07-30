package com.dragonsofmugloar.adventure.service;

import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dragonsofmugloar.adventure.dto.TaskResponse;
import com.dragonsofmugloar.adventure.util.TaskDecoder;

public class TaskSelector {

    private static final Logger log = LogManager.getLogger(TaskSelector.class);

    public TaskResponse selectTask(List<TaskResponse> tasks) {
        return tasks.stream()
            .map(task -> task.getEncrypted() != null ? TaskDecoder.decodeTaskResponse(task) : task)
            .filter(task -> !task.getMessage().startsWith("Steal super awesome diamond"))
            .sorted(Comparator.comparingInt(this::probabilityRank).thenComparing(TaskResponse::getReward, Comparator.reverseOrder()))
            .findFirst()
            .orElse(null);
    }

    private int probabilityRank(TaskResponse task) {
        String taskProbability = task.getProbability().toLowerCase();
        return switch (taskProbability) {
            case "piece of cake" -> 1;
            case "walk in the park" -> 2;
            case "sure thing" -> 3;
            case "quite likely" -> 4;
            case "rather detrimental" -> 5;
            case "hmmm...." -> 6;
            case "playing with fire" -> 7;
            case "risky" -> 8;
            case "gamble" -> 9;
            case "suicide mission" -> 10;
            case "impossible" -> 11;
            default -> {
                log.warn("Unknown probability" + taskProbability);
                yield 12;
            }
        };
    }
}

