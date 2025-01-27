package controllers;

import models.Task;
import models.StatusTask;
import models.TypeOfTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.now(), Duration.ofHours(1));
        task2 = new Task("Task 2", TypeOfTask.TASK, "Desc", 2, StatusTask.NEW,
                LocalDateTime.now(), Duration.ofHours(1));
        task3 = new Task("Task 3", TypeOfTask.TASK, "Desc", 3, StatusTask.NEW,
                LocalDateTime.now(), Duration.ofHours(1));
    }

    @Test
    void shouldHandleEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldHandleDuplicateHistoryEntries() {
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "Дублирование задач не должно происходить");
    }

    @Test
    void shouldRemoveFromHistoryCorrectly() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getIdTask());
        assertEquals(2, historyManager.getHistory().size(), "Задача не удалена корректно");

        historyManager.remove(task3.getIdTask());
        assertEquals(1, historyManager.getHistory().size(), "Удаление последнего элемента не работает корректно");

        historyManager.remove(task2.getIdTask());
        assertTrue(historyManager.getHistory().isEmpty(), "Удаление всех элементов не работает корректно");
    }
}
