package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;


    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
        } else {
            exchange.sendResponseHeaders(405, 0);
        }
        exchange.close();
    }
}