package com.dragonsofmugloar.adventure.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dragonsofmugloar.adventure.dto.TaskResponse;

class TaskSelectorTest {

    private TaskSelector selector;

    @BeforeEach
    void setUp() {
        selector = new TaskSelector();
    }

    @Test
    void shouldSelectBestTask() {
        TaskResponse t1 = new TaskResponse();
        t1.setAdId("1");
        t1.setProbability("sure thing");
        t1.setMessage("Complete the quest");
        t1.setReward(100);

        TaskResponse t2 = new TaskResponse();
        t2.setAdId("2");
        t2.setProbability("walk in the park");
        t2.setMessage("Help the villagers");
        t2.setReward(50);

        List<TaskResponse> tasks = List.of(t1, t2);
        TaskResponse selected = selector.selectTask(tasks);

        assertNotNull(selected);
        assertEquals("2", selected.getAdId());
    }

    @Test
    void shouldReturnNullWhenNoTasks() {
        List<TaskResponse> tasks = List.of();
        TaskResponse selected = selector.selectTask(tasks);

        assertEquals(null, selected);
    }

    @Test
    void shouldFilterTrapTasks() {
        TaskResponse t1 = new TaskResponse();
        t1.setAdId("1");
        t1.setProbability("piece of cake");
        t1.setReward(100);
        t1.setMessage("Steal super awesome diamond");

        List<TaskResponse> tasks = List.of(t1);
        TaskResponse selected = selector.selectTask(tasks);

        assertEquals(null, selected, "Should filter out trap task");
    }
}