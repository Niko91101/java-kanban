package http.handlers;

import com.google.gson.Gson;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import http.HttpTaskServer;
import models.Epic;
import models.StatusTask;
import models.TypeOfTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerEpicsTest {
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
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", TypeOfTask.EPIC, "Описание эпика", null, StatusTask.NEW);

        String epicJson = BaseHttpHandler.gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Ошибка при создании эпика");

        assertEquals(1, manager.getAllEpics().size(), "Эпик не был добавлен");
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", TypeOfTask.EPIC, "Описание эпика", 1, StatusTask.NEW);
        manager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении эпиков");

        Epic[] epics = BaseHttpHandler.gson.fromJson(response.body(), Epic[].class);
        assertEquals(1, epics.length, "Некорректное количество эпиков");
        assertEquals("Эпик", epics[0].getNameTask(), "Некорректное имя эпика");
    }

    @Test
    public void testAddAndDeleteEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", TypeOfTask.EPIC, "Описание", null, StatusTask.NEW);
        Epic epic2 = new Epic("Эпик 2", TypeOfTask.EPIC, "Описание", null, StatusTask.NEW);

        sendPostRequestAndValidate(epic1, "epics", 201);
        sendPostRequestAndValidate(epic2, "epics", 201);

        List<Epic> epics = manager.getAllEpics();
        assertEquals(2, epics.size(), "Эпики добавлены некорректно");

        // Удаление одного эпика
        sendDeleteRequestAndValidate(1, "epics", 200);
        epics = manager.getAllEpics();
        assertEquals(1, epics.size(), "Эпик не удалился");
    }

    private void sendPostRequestAndValidate(Epic epic, String endpoint, int expectedStatus) throws IOException, InterruptedException {
        String json = BaseHttpHandler.gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), "Некорректный статус при добавлении эпика");
    }

    private void sendDeleteRequestAndValidate(int id, String endpoint, int expectedStatus) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + endpoint + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), "Некорректный статус при удалении эпика");
    }

}