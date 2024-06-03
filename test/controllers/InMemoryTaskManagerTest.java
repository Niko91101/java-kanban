package controllers;

import models.Epic;
import models.StatusTask;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getDefault();

    Task task1 = new Task("Поиграть в футбол", "Завтра в 15,00", 0, StatusTask.NEW);
    int task1Id = manager.addTask(task1);

    Task task2 = new Task("Закончить это ТЗ", "Желательно завтра", 0, StatusTask.NEW);
    int task2Id = manager.addTask(task2);


    Epic epic1 = new Epic("Завершить этот год удачно", "Планы на год", 0,
            new ArrayList<>());
    int epic1Id = manager.addEpic(epic1);


    Subtask subtask1 = new Subtask("Успешно сдать ТЗ № 4", "Сейчас на верном пути", 0,
            StatusTask.IN_PROGRESS, epic1Id);
    int subtask1Id = manager.addSubtask(subtask1);

    Subtask subtask2 = new Subtask("Успешно сдать ТЗ № 5", "Думаю там лютый пиздарез", 0,
            StatusTask.DONE, epic1Id);
    int Subtask2Id = manager.addSubtask(subtask2);




    @Test
    void deleteTasks() {
        manager.deleteTasks();
        Assertions.assertTrue(manager.getAllTasks().isEmpty());

    }

    @Test
    void deleteSubtasks() {
    }

    @Test
    void deleteEpics() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getSubtaskById() {
    }

    @Test
    void getEpicById() {
    }

    @Test
    void addTask() {
    }

    @Test
    void addEpic() {
    }

    @Test
    void addSubtask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubtasks() {
    }

    @Test
    void deleteTaskId() {
    }

    @Test
    void deleteEpicId() {
    }

    @Test
    void deleteSubtaskId() {
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void getAllEpics() {
    }

    @Test
    void getAllSubtasks() {
    }

    @Test
    void getSubtasksFromEpic() {
    }

    @Test
    void getHistory() {
    }
}