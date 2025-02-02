package http.handlers;

import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import http.HttpTaskServer;
import models.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static http.handlers.BaseHttpHandler.gson;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerSubtasksTest {
    private static HttpTaskServer taskServer;
    private static TaskManager manager;
    private static final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", TypeOfTask.EPIC, "Описание эпика", 1, StatusTask.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", TypeOfTask.SUBTASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 12, 0), Duration.ofHours(1), epic.getIdTask());

        String subtaskJson = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Ошибка при создании подзадачи");

        assertEquals(1, manager.getAllSubtasks().size(), "Подзадача не была добавлена");
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", TypeOfTask.EPIC, "Описание эпика", 1, StatusTask.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", TypeOfTask.SUBTASK, "Описание", 2, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 12, 0), Duration.ofHours(1), epic.getIdTask());
        manager.addSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении подзадач");

        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(1, subtasks.length, "Некорректное количество подзадач");
        assertEquals("Подзадача", subtasks[0].getNameTask(), "Некорректное имя подзадачи");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", TypeOfTask.EPIC, "Описание эпика", 1, StatusTask.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", TypeOfTask.SUBTASK, "Описание", 2, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 12, 0), Duration.ofHours(1), epic.getIdTask());
        manager.addSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при удалении подзадачи");

        assertNull(manager.getSubtaskById(2), "Подзадача не была удалена");
    }

    @Test
    public void testAddSubtasksWithDifferentCases() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", TypeOfTask.EPIC, "Описание", null, StatusTask.NEW);
        manager.addEpic(epic);

        Subtask validSubtask = new Subtask("Обычная подзадача", TypeOfTask.SUBTASK, "Описание", null,
                StatusTask.NEW, LocalDateTime.of(2025, 2, 1, 14, 0), Duration.ofHours(2), epic.getIdTask());

        Subtask noEpicSubtask = new Subtask("Без эпика", TypeOfTask.SUBTASK, "Описание", null,
                StatusTask.NEW, LocalDateTime.of(2025, 2, 1, 15, 0), Duration.ofHours(1), 999);

        sendPostRequestAndValidate(validSubtask, "subtasks", 201);
        sendPostRequestAndValidate(noEpicSubtask, "subtasks", 404);

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size(), "Некорректное количество подзадач");
    }

    private void sendPostRequestAndValidate(Subtask subtask, String path, int expectedStatus) throws IOException, InterruptedException {
        String json = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), "Некорректный статус при добавлении: " + subtask.getNameTask());
    }
}