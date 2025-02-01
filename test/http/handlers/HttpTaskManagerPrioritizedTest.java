package http.handlers;

import com.google.gson.Gson;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import http.HttpTaskServer;
import models.Epic;
import models.StatusTask;
import models.Subtask;
import models.Task;
import models.TypeOfTask;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerPrioritizedTest {
    private static HttpTaskServer taskServer;
    private static TaskManager manager;
    private static final Gson gson = BaseHttpHandler.gson;
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
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", TypeOfTask.TASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача 2", TypeOfTask.TASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 12, 0), Duration.ofHours(1));
        Task task3 = new Task("Задача 3 (без времени)", TypeOfTask.TASK, "Описание", null, StatusTask.NEW);

        sendPostRequestAndValidate(task1, "tasks", 201);
        sendPostRequestAndValidate(task2, "tasks", 201);
        sendPostRequestAndValidate(task3, "tasks", 201);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении приоритетного списка");

        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, prioritizedTasks.length, "Некорректное количество задач в приоритетном списке");

        assertEquals("Задача 1", prioritizedTasks[0].getNameTask(), "Некорректный порядок задач");
        assertEquals("Задача 2", prioritizedTasks[1].getNameTask(), "Некорректный порядок задач");
    }

    @Test
    public void testGetPrioritizedSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", TypeOfTask.EPIC, "Описание", null, StatusTask.NEW);
        sendPostRequestAndValidate(epic, "epics", 201);

        Subtask subtask1 = new Subtask("Подзадача 1", TypeOfTask.SUBTASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 11, 0), Duration.ofHours(2), 1);
        Subtask subtask2 = new Subtask("Подзадача 2", TypeOfTask.SUBTASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 15, 0), Duration.ofHours(1), 1);
        Subtask subtask3 = new Subtask("Подзадача 3", TypeOfTask.SUBTASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 8, 0), Duration.ofHours(1), 1); // Раньше всех

        sendPostRequestAndValidate(subtask1, "subtasks", 201);
        sendPostRequestAndValidate(subtask2, "subtasks", 201);
        sendPostRequestAndValidate(subtask3, "subtasks", 201);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении приоритетного списка");

        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(3, prioritizedTasks.length, "Некорректное количество подзадач в приоритетном списке");

        assertEquals("Подзадача 3", prioritizedTasks[0].getNameTask(), "Некорректный порядок подзадач");
        assertEquals("Подзадача 1", prioritizedTasks[1].getNameTask(), "Некорректный порядок подзадач");
        assertEquals("Подзадача 2", prioritizedTasks[2].getNameTask(), "Некорректный порядок подзадач");
    }

    @Test
    public void testEpicsNotInPrioritizedList() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", TypeOfTask.EPIC, "Описание", null, StatusTask.NEW);
        Epic epic2 = new Epic("Эпик 2", TypeOfTask.EPIC, "Описание", null, StatusTask.NEW);
        Epic epic3 = new Epic("Эпик 3", TypeOfTask.EPIC, "Описание", null, StatusTask.NEW);

        sendPostRequestAndValidate(epic1, "epics", 201);
        sendPostRequestAndValidate(epic2, "epics", 201);
        sendPostRequestAndValidate(epic3, "epics", 201);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении приоритетного списка");

        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(0, prioritizedTasks.length, "Эпики не должны попадать в приоритетный список");
    }

    private void sendPostRequestAndValidate(Task task, String endpoint, int expectedStatus) throws IOException, InterruptedException {
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), "Некорректный статус при добавлении задачи");
    }
}