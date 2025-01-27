package models;

import models.Epic;
import models.StatusTask;
import models.TypeOfTask;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    public void testEmptySubtaskList() {
        Epic epic = new Epic("Epic Task", TypeOfTask.EPIC, "Epic Description", 1, StatusTask.NEW);
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
    }

    @Test
    public void testSetEndTime() {
        Epic epic = new Epic("Epic Task", TypeOfTask.EPIC, "Epic Description", 1, StatusTask.NEW);
        epic.setEndTime(LocalDateTime.of(2023, 1, 1, 18, 0));
        assertEquals(LocalDateTime.of(2023, 1, 1, 18, 0), epic.getEndTime());
    }

    @Test
    public void shouldInitializeWithEmptySubtasks() {
        Epic epic = new Epic("Epic Task", TypeOfTask.EPIC, "Epic Description", 1, StatusTask.NEW);
        assertTrue(epic.getSubtaskId().isEmpty());
    }

    @Test
    public void shouldAllowAddingSubtaskIds() {
        Epic epic = new Epic("Epic Task", TypeOfTask.EPIC, "Epic Description", 1, StatusTask.NEW);
        epic.getSubtaskId().add(101);
        assertEquals(1, epic.getSubtaskId().size());
        assertTrue(epic.getSubtaskId().contains(101));
    }
}
