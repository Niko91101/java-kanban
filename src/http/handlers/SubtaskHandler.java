package http.handlers;

import controllers.TaskManager;
import models.Subtask;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;


    public SubtaskHandler(TaskManager taskManager) {
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
        if (splitPath.length == 2) { // GET /subtasks - Получить все подзадачи
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            subtasks.forEach(subtask -> {
                if (subtask.getDuration() == null) subtask.setDuration(java.time.Duration.ZERO);
                if (subtask.getStartTime() == null) subtask.setStartTime(null);
            });
            sendText(exchange, gson.toJson(subtasks), 200);
        } else if (splitPath.length == 3) { // GET /subtasks/{id} - Получить подзадачу по id
            int id = Integer.parseInt(splitPath[2]);
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                if (subtask.getDuration() == null) subtask.setDuration(java.time.Duration.ZERO);
                if (subtask.getStartTime() == null) subtask.setStartTime(null);
                sendText(exchange, gson.toJson(subtask), 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }


    private void handlePostRequest(HttpExchange exchange) throws IOException {
        Subtask subtask = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Subtask.class);

        try {
            Integer id = taskManager.addSubtask(subtask);
            sendText(exchange, "Сабтаск создан с ID: " + id, 201);
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

    // 🔹 Обработка DELETE запросов
    private void handleDeleteRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 3) { // DELETE /subtasks/{id}
            int id = Integer.parseInt(splitPath[2]);
            taskManager.deleteSubtaskId(id);
            sendText(exchange, "Подзадача удалена.", 200);
        } else {
            sendText(exchange, "Некорректный запрос на удаление", 400);
        }
    }
}