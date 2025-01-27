package controllers;

import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void shouldAddAndRetrieveTask() {
        Task task = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(1));
        taskManager.addTask(task);

        assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    void shouldDeleteTaskById() {
        Task task = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(1));
        taskManager.addTask(task);

        taskManager.deleteTaskId(1);
        assertNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldHandleEmptyTaskList() {
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void shouldNotAddOverlappingTasks() {
        Task task1 = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(2));
        Task task2 = new Task("Task 2", TypeOfTask.TASK, "Desc", 2, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofHours(1));

        taskManager.addTask(task1);
        Integer result = taskManager.addTask(task2);

        assertNull(result, "Overlapping task should not be added");
    }

    @Test
    void shouldReturnTasksSortedByStartTime() {
        Task task1 = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(1));
        Task task2 = new Task("Task 2", TypeOfTask.TASK, "Desc", 2, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 5, 0), Duration.ofHours(3));

        taskManager.addTask(task2);
        taskManager.addTask(task1);

        List<Task> sortedTasks = taskManager.getPrioritizedTasks();
        assertEquals(task2, sortedTasks.get(0));
        assertEquals(task1, sortedTasks.get(1));
    }
}