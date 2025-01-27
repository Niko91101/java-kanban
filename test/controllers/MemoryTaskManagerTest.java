package controllers;

import models.*;


import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryTaskManagerTest {
    @Test
    public void shouldAddTaskCorrectly() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(1));
        Integer id = manager.addTask(task);
        assertNotNull(id);
        assertEquals(task, manager.getTaskById(id));
    }

    @Test
    public void shouldNotAddOverlappingTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(2));
        Task task2 = new Task("Task 2", TypeOfTask.TASK, "Desc", 2, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofHours(1));

        manager.addTask(task1);
        Integer result = manager.addTask(task2);
        assertNull(result, "Task with overlapping time should not be added");
    }


    @Test
    public void shouldReturnPrioritizedTasksCorrectly() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(2));
        Task task2 = new Task("Task 2", TypeOfTask.TASK, "Desc", 2, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 7, 0), Duration.ofHours(1));

        manager.addTask(task2);
        manager.addTask(task1);

        assertEquals(task2, manager.getPrioritizedTasks().get(0));
        assertEquals(task1, manager.getPrioritizedTasks().get(1));
    }

    @Test
    public void shouldRemoveTaskById() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Task 1", TypeOfTask.TASK, "Description", 1, StatusTask.NEW,
                LocalDateTime.now(), Duration.ofHours(1));
        manager.addTask(task);

        manager.deleteTaskId(task.getIdTask());
        assertNull(manager.getTaskById(task.getIdTask()));
    }

    @Test
    public void shouldRemoveSubtaskAndUpdateEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", TypeOfTask.EPIC, "Epic Description", 1, StatusTask.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Subtask Description", 2,
                StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdTask());
        manager.addSubtask(subtask);

        manager.deleteSubtaskId(subtask.getIdTask());

        assertTrue(epic.getSubtaskId().isEmpty());
    }

    @Test
    public void shouldReturnTasksSortedByStartTime() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(1));
        Task task2 = new Task("Task 2", TypeOfTask.TASK, "Desc", 2, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 6, 59), Duration.ofHours(2));

        manager.addTask(task2);
        manager.addTask(task1);

        List<Task> sortedTasks = manager.getPrioritizedTasks();
        assertEquals(task2, sortedTasks.get(0));
        assertEquals(task1, sortedTasks.get(1));
    }

    @Test
    public void shouldNotIncludeTasksWithoutStartTimeInPriorityList() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task taskWithoutTime = new Task("Task without time", TypeOfTask.TASK, "No Description", 3, StatusTask.NEW);
        manager.addTask(taskWithoutTime);

        assertFalse(manager.getPrioritizedTasks().contains(taskWithoutTime));
    }

    @Test
    public void shouldUpdateTaskCorrectly() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Initial Task", TypeOfTask.TASK, "Initial Description", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofHours(1));
        manager.addTask(task);

        Task updatedTask = new Task("Updated Task", TypeOfTask.TASK, "Updated Description", 1, StatusTask.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 1, 12, 0), Duration.ofHours(2));
        manager.updateTask(updatedTask);

        Task retrievedTask = manager.getTaskById(1);
        assertEquals("Updated Task", retrievedTask.getNameTask());
        assertEquals(StatusTask.IN_PROGRESS, retrievedTask.getStatus());
    }

    @Test
    public void shouldUpdateEpicAfterSubtaskChange() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", TypeOfTask.EPIC, "Epic Description", 1, StatusTask.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Subtask Description", 2,
                StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdTask());
        manager.addSubtask(subtask);

        assertEquals(StatusTask.NEW, manager.getEpicById(epic.getIdTask()).getStatus());

        subtask.setStatus(StatusTask.DONE);
        manager.updateSubtasks(subtask);

        System.out.println("Epic subtask list: " + manager.getEpicById(epic.getIdTask()).getSubtaskId());
        System.out.println("Before update: " + epic.getStatus());
        System.out.println("After update: " + manager.getEpicById(epic.getIdTask()).getStatus());

        assertEquals(StatusTask.DONE, manager.getEpicById(epic.getIdTask()).getStatus());
    }
}
