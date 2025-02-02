package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EpicStatusCalculationTest {
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;
    private Map<Integer, Subtask> subtasks;

    @BeforeEach
    void setUp() {
        epic = new Epic("Epic Task", TypeOfTask.EPIC, "Epic Desc", 1, StatusTask.NEW);
        subtasks = new HashMap<>();
    }

    private void addSubtasksToEpic(Subtask... subtasksArray) {
        for (Subtask subtask : subtasksArray) {
            epic.getSubtaskId().add(subtask.getIdTask());
            subtasks.put(subtask.getIdTask(), subtask);
        }
        updateEpicStatus();
    }

    private void updateEpicStatus() {
        if (epic.getSubtaskId().isEmpty()) {
            epic.setStatus(StatusTask.NEW);
            return;
        }

        boolean allDone = epic.getSubtaskId().stream()
                .map(subtasks::get)
                .allMatch(subtask -> subtask.getStatus() == StatusTask.DONE);

        boolean allNew = epic.getSubtaskId().stream()
                .map(subtasks::get)
                .allMatch(subtask -> subtask.getStatus() == StatusTask.NEW);

        if (allDone) {
            epic.setStatus(StatusTask.DONE);
        } else if (allNew) {
            epic.setStatus(StatusTask.NEW);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

    @Test
    void shouldSetNewStatusWhenAllSubtasksAreNew() {
        subtask1 = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Desc", 2, StatusTask.NEW, null, null, epic.getIdTask());
        subtask2 = new Subtask("Subtask 2", TypeOfTask.SUBTASK, "Desc", 3, StatusTask.NEW, null, null, epic.getIdTask());

        addSubtasksToEpic(subtask1, subtask2);

        assertEquals(StatusTask.NEW, epic.getStatus());
    }

    @Test
    void shouldSetDoneStatusWhenAllSubtasksAreDone() {
        subtask1 = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Desc", 2, StatusTask.DONE, null, null, epic.getIdTask());
        subtask2 = new Subtask("Subtask 2", TypeOfTask.SUBTASK, "Desc", 3, StatusTask.DONE, null, null, epic.getIdTask());

        addSubtasksToEpic(subtask1, subtask2);

        assertEquals(StatusTask.DONE, epic.getStatus());
    }

    @Test
    void shouldSetInProgressWhenSubtasksAreNewAndDone() {
        subtask1 = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Desc", 2, StatusTask.NEW, null, null, epic.getIdTask());
        subtask2 = new Subtask("Subtask 2", TypeOfTask.SUBTASK, "Desc", 3, StatusTask.DONE, null, null, epic.getIdTask());

        addSubtasksToEpic(subtask1, subtask2);

        assertEquals(StatusTask.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void shouldSetInProgressWhenSubtasksAreInProgress() {
        subtask1 = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Desc", 2, StatusTask.IN_PROGRESS, null, null, epic.getIdTask());
        subtask2 = new Subtask("Subtask 2", TypeOfTask.SUBTASK, "Desc", 3, StatusTask.IN_PROGRESS, null, null, epic.getIdTask());

        addSubtasksToEpic(subtask1, subtask2);

        assertEquals(StatusTask.IN_PROGRESS, epic.getStatus());
    }
}
