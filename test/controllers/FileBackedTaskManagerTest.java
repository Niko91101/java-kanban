package controllers;

import models.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("test_tasks.csv"));
    }

    @Test
    void shouldSaveAndLoadTasksCorrectly() {
        Task task = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 15, 0), Duration.ofHours(1));

        taskManager.addTask(task);

        // Создание нового экземпляра, загрузка данных из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(new File("test_tasks.csv"));
        Task loadedTask = loadedManager.getTaskById(1);

        assertNotNull(loadedTask, "Задача должна быть загружена");
        assertEquals(task, loadedTask, "Загруженная задача должна совпадать с сохраненной");
    }

    @Test
    void shouldSaveAndLoadEpicsWithSubtasksCorrectly() {
        Epic epic = new Epic("Epic Task", TypeOfTask.EPIC, "Epic Desc", 1, StatusTask.NEW);
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Subtask Desc", 2,
                StatusTask.NEW, LocalDateTime.of(2024, 1, 2, 10, 0), Duration.ofHours(2), epic.getIdTask());
        taskManager.addSubtask(subtask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(new File("test_tasks.csv"));

        assertEquals(epic, loadedManager.getEpicById(1));
        assertEquals(subtask, loadedManager.getSubtaskById(2));
    }

    @Test
    void shouldSaveAndLoadEmptyTaskList() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();
        taskManager.save();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(new File("test_tasks.csv"));
        assertTrue(loadedManager.getAllTasks().isEmpty() &&
                        loadedManager.getAllEpics().isEmpty() &&
                        loadedManager.getAllSubtasks().isEmpty(),
                "Списки задач должны быть пустыми");
    }



    @Test
    void shouldSaveAndLoadTasksAfterDeletion() {
        Task task1 = new Task("Task 1", TypeOfTask.TASK, "Desc", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofHours(1));
        Task task2 = new Task("Task 2", TypeOfTask.TASK, "Desc", 2, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 11, 0), Duration.ofHours(1));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTaskId(1);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(new File("test_tasks.csv"));

        assertNull(loadedManager.getTaskById(1));
        assertNotNull(loadedManager.getTaskById(2));
    }

    @Test
    void shouldNotLoadFromEmptyFile() {
        File emptyFile = new File("empty_test.csv");
        try {
            emptyFile.createNewFile();
        } catch (Exception e) {
            fail("Не удалось создать пустой тестовый файл");
        }

        Exception exception = assertThrows(ManagerException.class, () ->
                FileBackedTaskManager.loadFromFile(emptyFile)
        );

        assertEquals("Файл пустой", exception.getMessage());
        emptyFile.delete();
    }
}

