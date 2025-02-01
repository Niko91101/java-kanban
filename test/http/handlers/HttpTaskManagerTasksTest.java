package http.handlers;

import com.google.gson.Gson;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import http.HttpTaskServer;
import models.Task;
import models.StatusTask;
import models.TypeOfTask;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {
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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Тестовая задача", TypeOfTask.TASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 10, 0), Duration.ofHours(1));

        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Тестовая задача", tasksFromManager.get(0).getNameTask(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Тестовая задача", TypeOfTask.TASK, "Описание", 1, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 10, 0), Duration.ofHours(1));
        manager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, tasks.length, "Некорректное количество задач");
        assertEquals("Тестовая задача", tasks[0].getNameTask(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Тестовая задача", TypeOfTask.TASK, "Описание", 1, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 10, 0), Duration.ofHours(1));
        manager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertNull(manager.getTaskById(1), "Задача не была удалена");
    }

    @Test
    public void testGetTaskWithInvalidId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/999")) // Несуществующий ID
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался код 404 при запросе несуществующей задачи");
    }

    @Test
    public void testAddMultipleTasks() throws IOException, InterruptedException {
        // 1️⃣ Задача с указанным временем
        Task taskWithTime = new Task("Задача с временем", TypeOfTask.TASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 10, 0), Duration.ofHours(1));

        // 2️⃣ Задача без времени
        Task taskWithoutTime = new Task("Задача без времени", TypeOfTask.TASK, "Описание", null, StatusTask.NEW);

        // 3️⃣ Задача с пересекающимся временем
        Task overlappingTask = new Task("Пересекающаяся задача", TypeOfTask.TASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 10, 30), Duration.ofHours(1));

        // Отправляем POST-запрос для каждой задачи
        sendPostRequestAndValidate(taskWithTime, 201);
        sendPostRequestAndValidate(taskWithoutTime, 201);
        sendPostRequestAndValidate(overlappingTask, 406); // Должен вернуть "Not Acceptable"

        // Проверяем, что в менеджере две задачи (третья не добавилась)
        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");

        // Проверяем, что приоритетный список содержит только одну задачу (без времени не должно быть)
        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(1, prioritizedTasks.size(), "В приоритетном списке неверное количество задач");
    }


    private void sendPostRequestAndValidate(Task task, int expectedStatus) throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), "Некорректный статус при добавлении задачи: " + task.getNameTask());
    }
}