package http.handlers;

import com.google.gson.Gson;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import http.HttpTaskServer;
import models.StatusTask;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpTaskManagerHistoryTest {
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
    @Order(1)
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Тестовая задача", TypeOfTask.TASK, "Описание", 1, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 10, 0), Duration.ofHours(1));
        manager.addTask(task);
        manager.getTaskById(1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении истории");

        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, history.length, "Некорректное количество задач в истории");
        assertEquals("Тестовая задача", history[0].getNameTask(), "Название задачи в истории не совпадает");
    }

    @Test
    @Order(2)
    public void testGetEmptyHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении пустой истории");

        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertEquals(0, history.length, "История должна быть пустой");
    }

    @Test
    public void testHistoryRetrieval() throws IOException, InterruptedException {
        Task task = new Task("Историческая задача", TypeOfTask.TASK, "Описание", null, StatusTask.NEW,
                LocalDateTime.of(2025, 2, 1, 18, 0), Duration.ofHours(1));
        manager.addTask(task);
        manager.getTaskById(1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении истории");

        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, history.length, "История возвращена некорректно");
    }
}
