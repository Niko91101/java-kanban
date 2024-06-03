package controllers;

import models.Epic;
import models.StatusTask;
import models.Subtask;
import models.Task;



import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getDefault();

    Task testTask1 = new Task("Тест", "Тест",
            0, StatusTask.NEW);
    int testTask1Id = manager.addTask(testTask1);

    Task testTask2 = new Task("Тест", "Тест",
            0, StatusTask.NEW);;
    int testTask2Id = manager.addTask(testTask2);


    Epic testEpic = new Epic("ТестЭпик", "Тест", 0,
            new ArrayList<>());
    int testEpicId = manager.addEpic(testEpic);

    Subtask testSubtask1 = new Subtask("ТестСабтаск1", "Сейчас на верном пути", 0,
            StatusTask.IN_PROGRESS, testEpicId);
    int testSubtask1Id = manager.addSubtask(testSubtask1);

    Subtask testSubtask2 = new Subtask("ТестСабтаск2", "Тест", 0,
            StatusTask.DONE, testEpicId);
    int testSubtaskId2 = manager.addSubtask(testSubtask2);



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
    void getSubtaskById() {
        assertEquals(testSubtask1, manager.getSubtaskById(4),
                "Сабтаск id которого передан не получен");
    }

    @Test
    void getEpicById() {
        assertEquals(testEpic, manager.getEpicById(3), "Эпик id которого передан не получен");
    }

    @Test
    void addTask() {
        Task testTask = new Task("Тест", "Тест", 0, StatusTask.NEW);
        manager.addTask(testTask);
        assertTrue(manager.getAllTasks().contains(testTask),
                "Таска переданная в метод addTast() отсутствует в списке");
    }

    @Test
    void addEpic() {
        Epic testEpic = new Epic("Тест", "Тест", 0,
                new ArrayList<>());
        manager.addEpic(testEpic);
        assertTrue(manager.getAllEpics().contains(testEpic),
                "Эпик переданный в метод addEpic() отсутствует в списке");
    }

    @Test
    void addSubtask() {
        Epic testEpic = new Epic("Тест", "Тест", 0,
                new ArrayList<>());
        int testEpicId = manager.addEpic(testEpic);
        Subtask testSubtask = new Subtask("Тест1", "Тест1", 0,
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
        manager.updateTask(new Task("Тест", "Тест", testTask1Id ,
                StatusTask.NEW));

        upTask1 = manager.getTaskById(testTask1Id);
        assertNotEquals(testTask1, upTask1, "Разные таски совпадают");
    }

    @Test
    void updateEpic() {
        manager.updateEpic(testEpic);
        Epic upEpic = manager.getEpicById(testEpicId);
        assertEquals(testEpic, upEpic, "Однотипные эпики не сопадают");

        manager.updateEpic(new Epic("Тест", "Тест", testEpicId,
                testEpic.getSubtaskId()));
        upEpic = manager.getEpicById(testEpicId);
        assertNotEquals(testEpic, upEpic, "Разные эпики совпадают");



    }

    @Test
    void deleteTaskId() {
        Task testTask = new Task("Тест", "Тест",
                0, StatusTask.NEW);

        int testTaskId = manager.addTask(testTask);

        manager.deleteTaskId(testTaskId);
        assertNotNull(manager.getAllTasks(), "Удалены все задачи");
        assertFalse(manager.getAllTasks().contains(testTask), "Переданная задача не удалена");
    }

    @Test
    void deleteEpicId() {
        Epic epicTest = new Epic("Тест", "Тест", 0,
                new ArrayList<>());
        int epicTestId = manager.addEpic(epicTest);

        manager.deleteEpicId(epicTestId);
        assertNotNull(manager.getAllEpics(), "Все эадачи удалены");
        assertFalse(manager.getAllEpics().contains(epicTest), "Переданная задача не удалена");
    }

    @Test
    void deleteSubtaskId() {
        Subtask subtaskTest = new Subtask("Тест", "Тест", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        int subtaskTestId = manager.addSubtask(subtaskTest);

        manager.deleteSubtaskId(subtaskTestId);
        assertNotNull(manager.getAllSubtasks(), "Все задачи удалены");
        assertFalse(manager.getAllSubtasks().contains(subtaskTest), "Переданная задача не удалена");
    }

    @Test
    void getAllTasks() {
        manager.deleteTasks();
        Task taskTest1 = new Task("Тест1", "Тест1", 0, StatusTask.NEW);
        int task1Id = manager.addTask(taskTest1);

        Task taskTest2 = new Task("Тест2", "Тест2", 0, StatusTask.NEW);
        int task2Id = manager.addTask(taskTest2);

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
        assertEquals(manager.getTaskById(task1Id),tasksTest.get(0), "Задачи не совпадают");
        assertEquals(manager.getTaskById(task2Id), tasksTest.get(1), "Задачи не совпадают");
    }

    @Test
    void getAllEpics() {
        manager.deleteEpics();
        Epic testEpic = new Epic("ТестЭпик", "Тест", 0,
                new ArrayList<>());
        int testEpicId = manager.addEpic(testEpic);
        assertEquals(1, manager.getAllEpics().size(),"Неверное количество задач");
        assertEquals("ТестЭпик", manager.getAllEpics().get(0).getNameTask(),
                "Имя задачи не совпадает" );

        ArrayList<Epic> testEpics = new ArrayList<>();
        for(Epic epic : manager.getAllEpics()) {
            testEpics.add(epic);
        }
        assertEquals(1, testEpics.size(), "Количество задач неверное");
        assertEquals(manager.getEpicById(testEpicId), testEpics.get(0), "Задачи не совпадают");

    }

    @Test
    void getAllSubtasks() {
        manager.deleteSubtasks();
        manager.deleteEpics();
        Epic testEpic = new Epic("ТестЭпик", "Тест", 0,
                new ArrayList<>());
        int testEpicId = manager.addEpic(testEpic);

        Subtask testSubtask1 = new Subtask("ТестСабтаск1", "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        int testSubtask1Id = manager.addSubtask(testSubtask1);

        Subtask testSubtask2 = new Subtask("ТестСабтаск2", "Тест", 0,
                StatusTask.DONE, testEpicId);
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
        Epic testEpic = new Epic("ТестЭпик", "Тест", 0,
                new ArrayList<>());
        int testEpicId = manager.addEpic(testEpic);

        Subtask testSubtask1 = new Subtask("ТестСабтаск1", "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        int testSubtask1Id = manager.addSubtask(testSubtask1);

        Subtask testSubtask2 = new Subtask("ТестСабтаск2", "Тест", 0,
                StatusTask.DONE, testEpicId);
        int testSubtaskId2 = manager.addSubtask(testSubtask2);

        assertEquals(2, manager.getAllSubtasks().size());
        assertEquals(testSubtask1, manager.getSubtasksFromEpic(testEpic).get(0), "Задачи не совпадают");
        assertEquals(testSubtask2, manager.getSubtasksFromEpic(testEpic).get(1), "Задачи не совпадают");

    }

    @Test
    void getHistory() {
        assertTrue(manager.getHistory().isEmpty(), "Список истории задач не пуст");
        manager.getTaskById(testTask1Id);
        manager.getSubtaskById(testSubtask1Id);
        manager.getEpicById(testEpicId);
        manager.getTaskById(testTask1Id);
        manager.getEpicById(testEpicId);
        manager.getEpicById(testEpicId);
        manager.getEpicById(testEpicId);
        manager.getEpicById(testEpicId);
        manager.getEpicById(testEpicId);
        manager.getEpicById(testEpicId);
        manager.getEpicById(testEpicId);


        assertEquals(10, manager.getHistory().size(), "Количество задач не совпадает");
        assertEquals(testSubtask1, manager.getHistory().get(0),
                "Первая задача в истории задач не соотвествует");


    }
}