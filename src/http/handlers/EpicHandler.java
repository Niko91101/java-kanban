package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import models.Epic;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().split("/");

        try {
            if ("GET".equalsIgnoreCase(method)) {
                sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
            } else if ("POST".equalsIgnoreCase(method)) {
                Epic epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Epic.class);
                taskManager.addEpic(epic);
                sendText(exchange, "Epic created.", 201);
            } else if ("DELETE".equalsIgnoreCase(method) && splitPath.length == 3) {
                int id = Integer.parseInt(splitPath[2]);
                taskManager.deleteEpicId(id);
                sendText(exchange, "Epic deleted.", 200);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
