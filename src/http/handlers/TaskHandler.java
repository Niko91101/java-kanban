package http.handlers;

import controllers.TaskManager;
import models.Task;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().split("/");

        try {
            if ("GET".equalsIgnoreCase(method)) {
                handleGetRequest(exchange, splitPath);
            } else if ("POST".equalsIgnoreCase(method)) {
                handlePostRequest(exchange);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                handleDeleteRequest(exchange, splitPath);
            } else {
                sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        } finally {
            exchange.close();
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) { // GET /tasks - Получить все задачи
            List<Task> tasks = taskManager.getAllTasks();
            tasks.forEach(task -> {
                if (task.getDuration() == null) task.setDuration(java.time.Duration.ZERO);
                if (task.getStartTime() == null) task.setStartTime(null);
            });
            sendText(exchange, gson.toJson(tasks), 200);
        } else if (splitPath.length == 3) { // GET /tasks/{id} - Получить задачу по id
            int id = Integer.parseInt(splitPath[2]);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                if (task.getDuration() == null) task.setDuration(java.time.Duration.ZERO);
                if (task.getStartTime() == null) task.setStartTime(null);
                sendText(exchange, gson.toJson(task), 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        Task task = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Task.class);

        if (task.getDuration() == null) task.setDuration(java.time.Duration.ZERO);
        if (task.getStartTime() == null) task.setStartTime(null);

        Integer id = taskManager.addTask(task);
        if (id != null) {
            sendText(exchange, "Задача создана с ID: " + id, 201);
        } else {
            sendHasInteractions(exchange);
        }
    }

    // 🔹 Обработка DELETE запросов
    private void handleDeleteRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 3) { // DELETE /tasks/{id}
            int id = Integer.parseInt(splitPath[2]);
            taskManager.deleteTaskId(id);
            sendText(exchange, "Задача удалена.", 200);
        } else {
            sendText(exchange, "Некорректный запрос на удаление", 400);
        }
    }
}
