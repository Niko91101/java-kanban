package controllers;

import models.*;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void shouldCorrectlyHandleEpicAndSubtasks() {
        Epic epic = new Epic("Epic 1", TypeOfTask.EPIC, "Epic Desc", 1, StatusTask.NEW);
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Subtask Desc", 2,
                StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdTask());
        taskManager.addSubtask(subtask);

        assertEquals(1, epic.getSubtaskId().size());
        assertEquals(subtask.getIdTask(), epic.getSubtaskId().get(0));
    }
}
