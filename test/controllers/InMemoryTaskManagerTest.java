package controllers;

import models.*;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getDefault();


    Task testTask1 = new Task("Поиграть в футбол", TypeOfTask.TASK, "Завтра в 15,00", 0, StatusTask.NEW);
    int testTask1Id = manager.addTask(testTask1);


    Task testTask2 = new Task("Закончить это ТЗ", TypeOfTask.TASK, "Желательно завтра", 0, StatusTask.NEW);
    int testTask2Id = manager.addTask(testTask2);


    Epic testEpic = new Epic("Завершить этот год удачно", TypeOfTask.EPIC, StatusTask.NEW, "Планы на год", 0,
            new ArrayList<>());
    int testEpicId = manager.addEpic(testEpic);


    Subtask testSubtask1 = new Subtask("Успешно сдать ТЗ № 4", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
            StatusTask.IN_PROGRESS, testEpicId);


    Subtask testSubtask2 = new Subtask("Успешно сдать ТЗ № 5", TypeOfTask.SUBTASK, "В процессе", 0,
            StatusTask.DONE, testEpicId);


    @Test
    void deleteTasks() {
        manager.deleteTasks();
        assertTrue(manager.getAllTasks().isEmpty(), "Список тасков не пустой");

    }

    @Test
    void deleteSubtasks() {
        manager.deleteSubtasks();
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список сабтасков не пустой");
    }

    @Test
    void deleteEpics() {
        manager.deleteEpics();
        assertTrue(manager.getAllEpics().isEmpty(), "Список эпиков не пустой");
    }

    @Test
    void getTaskById() {
        assertEquals(testTask1, manager.getTaskById(1), "Таск id которого передан не получен");
    }

    @Test
    void getEpicById() {
        assertEquals(testEpic, manager.getEpicById(3), "Эпик id которого передан не получен");
    }

    @Test
    void addTask() {
        Task testTask1 = new Task("Поиграть в футбол", TypeOfTask.TASK, "Завтра в 15,00", 0, StatusTask.NEW);

        manager.addTask(testTask1);
        assertTrue(manager.getAllTasks().contains(testTask1),
                "Таска переданная в метод addTast() отсутствует в списке");
    }

    @Test
    void addEpic() {
        Epic testEpic = new Epic("Тест", TypeOfTask.EPIC, StatusTask.NEW, "sdsads",
                0, new ArrayList<>());
        manager.addEpic(testEpic);
        assertTrue(manager.getAllEpics().contains(testEpic),
                "Эпик переданный в метод addEpic() отсутствует в списке");
    }

    @Test
    void addSubtask() {
        Epic testEpic = new Epic("Тест", TypeOfTask.EPIC, StatusTask.NEW, "sdsads",
                0, new ArrayList<>());
        int testEpicId = manager.addEpic(testEpic);
        Subtask testSubtask = new Subtask("Тест1", TypeOfTask.SUBTASK, "Test", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        manager.addSubtask(testSubtask);
        assertTrue(manager.getAllSubtasks().contains(testSubtask),
                "Сабтаск передаваемый в метод addSubtask() в списке отсутствует");
    }

    @Test
    void updateTask() {
        manager.updateTask(testTask1);
        Task upTask1 = manager.getTaskById(testTask1Id);

        assertEquals(testTask1, upTask1, "Однотипные таски не совпадают");
        manager.updateTask(new Task("Поиграть в футбол", TypeOfTask.TASK,
                "Завтра в 15,00", upTask1.getIdTask(), StatusTask.NEW));


        upTask1 = manager.getTaskById(testTask1Id);
        assertNotEquals(testTask1, upTask1, "Разные таски совпадают");
    }


    @Test
    void deleteTaskId() {
        Task testTask = new Task("Поиграть в футбол", TypeOfTask.TASK, "Завтра в 15,00", 0, StatusTask.NEW);

        int testTaskId = manager.addTask(testTask);

        manager.deleteTaskId(testTaskId);
        assertNotNull(manager.getAllTasks(), "Удалены все задачи");
        assertFalse(manager.getAllTasks().contains(testTask), "Переданная задача не удалена");
    }

    @Test
    void deleteEpicId() {

        Epic testEpic = new Epic("Тест", TypeOfTask.EPIC, StatusTask.NEW, "sdsads",
                0, new ArrayList<>());
        int epicTestId = manager.addEpic(testEpic);

        manager.deleteEpicId(epicTestId);
        assertNotNull(manager.getAllEpics(), "Все эадачи удалены");
        assertFalse(manager.getAllEpics().contains(testEpic), "Переданная задача не удалена");
    }

    @Test
    void deleteSubtaskId() {
        Subtask subtaskTest = new Subtask("Тест", TypeOfTask.SUBTASK, "Test", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        int subtaskTestId = manager.addSubtask(subtaskTest);

        manager.deleteSubtaskId(subtaskTestId);
        assertNotNull(manager.getAllSubtasks(), "Все задачи удалены");
        assertFalse(manager.getAllSubtasks().contains(subtaskTest), "Переданная задача не удалена");
    }

    @Test
    void getAllTasks() {
        manager.deleteTasks();
        Task testTask1 = new Task("Тест1", TypeOfTask.TASK, "Завтра в 15,00", 0, StatusTask.NEW);
        int task1Id = manager.addTask(testTask1);

        Task testTask2 = new Task("Тест2", TypeOfTask.TASK, "Завтра в 15,00", 0, StatusTask.NEW);
        int task2Id = manager.addTask(testTask2);

        assertEquals(2, manager.getAllTasks().size(), "Неверное количество задач");
        assertEquals("Тест1", manager.getTaskById(task1Id).getNameTask(),
                "Неверное имя ожидаемоей задачи");
        assertEquals("Тест2", manager.getTaskById(task2Id).getNameTask(),
                "Неверное имя ожидаемоей задачи");

        ArrayList<Task> tasksTest = new ArrayList<>();

        for (Task task : manager.getAllTasks()) {
            tasksTest.add(task);
        }
        assertEquals(2, tasksTest.size());
        assertEquals(manager.getTaskById(task1Id), tasksTest.get(0), "Задачи не совпадают");
        assertEquals(manager.getTaskById(task2Id), tasksTest.get(1), "Задачи не совпадают");
    }

    @Test
    void getAllEpics() {
        manager.deleteEpics();
        Epic testEpic = new Epic("ТестЭпик", TypeOfTask.EPIC, StatusTask.NEW, "sdsads",
                0, new ArrayList<>());
        int testEpicId = manager.addEpic(testEpic);
        assertEquals(1, manager.getAllEpics().size(), "Неверное количество задач");
        assertEquals("ТестЭпик", manager.getAllEpics().get(0).getNameTask(),
                "Имя задачи не совпадает");

        ArrayList<Epic> testEpics = new ArrayList<>();
        for (Epic epic : manager.getAllEpics()) {
            testEpics.add(epic);
        }
        assertEquals(1, testEpics.size(), "Количество задач неверное");
        assertEquals(manager.getEpicById(testEpicId), testEpics.get(0), "Задачи не совпадают");
    }

    @Test
    void getAllSubtasks() {
        manager.deleteSubtasks();
        manager.deleteEpics();
        Epic testEpic = new Epic("Тест", TypeOfTask.EPIC, StatusTask.NEW, "sdsads",
                0, new ArrayList<>());
        int testEpicId = manager.addEpic(testEpic);

        Subtask testSubtask1 = new Subtask("ТестСабтаск1", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, testEpicId);

        Subtask testSubtask2 = new Subtask("ТестСабтаск2", TypeOfTask.SUBTASK, "В процессе", 0,
                StatusTask.DONE, testEpicId);
        int testSubtask1Id = manager.addSubtask(testSubtask1);
        int testSubtaskId2 = manager.addSubtask(testSubtask2);

        assertEquals(2, manager.getAllSubtasks().size(), "Неверное количество задач");
        assertEquals("ТестСабтаск1", manager.getSubtaskById(testSubtask1Id).getNameTask(),
                "Имя задач не совпадает");
        assertEquals("ТестСабтаск2", manager.getSubtaskById(testSubtaskId2).getNameTask(),
                "Имя задач не совпадает");

        ArrayList<Subtask> testSubtasks = new ArrayList<>();
        for (Subtask subtask : manager.getAllSubtasks()) {
            testSubtasks.add(subtask);
        }

        assertEquals(2, testSubtasks.size(), "Неверное количество задач");
        assertEquals(manager.getSubtaskById(testSubtask1Id), testSubtasks.get(0), "Задачи не совпадают");
        assertEquals(manager.getSubtaskById(testSubtaskId2), testSubtasks.get(1), "Задачи не совпадают");

    }

    @Test
    void getSubtasksFromEpic() {
        manager.deleteSubtasks();
        manager.deleteEpics();
        Epic testEpic = new Epic("Тест", TypeOfTask.EPIC, StatusTask.NEW, "sdsads",
                0, new ArrayList<>());
        int epicTestId = manager.addEpic(testEpic);

        Subtask testSubtask1 = new Subtask("Успешно сдать ТЗ № 4", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, epicTestId);

        Subtask testSubtask2 = new Subtask("Успешно сдать ТЗ № 5", TypeOfTask.SUBTASK, "В процессе", 0,
                StatusTask.DONE, epicTestId);
        int testSubtask1Id = manager.addSubtask(testSubtask1);
        int testSubtaskId2 = manager.addSubtask(testSubtask2);

        assertEquals(2, manager.getAllSubtasks().size());
        assertEquals(testSubtask1, manager.getSubtasksFromEpic(testEpic).get(0), "Задачи не совпадают");
        assertEquals(testSubtask2, manager.getSubtasksFromEpic(testEpic).get(1), "Задачи не совпадают");
    }

    @Test
    public void historyDuplication() {
        manager.getTaskById(testTask1Id);
        manager.getTaskById(testTask1Id);

        List<Task> history = manager.getHistory();
        assertNotEquals(Collections.emptyList(), history, "История пуста.");
        assertEquals(1, history.size(), "Дубль в истории.");

    }
}