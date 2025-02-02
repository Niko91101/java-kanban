package http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.utils.DurationAdapter;
import http.utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler {
    protected static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders()
                .add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(Optional.ofNullable(response).orElse("".getBytes(StandardCharsets.UTF_8)));
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"error\": \"Not Found\"}", 404);
    }

    protected void sendHasConflict(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"error\": \"Task overlap detected\"}", 406);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "Ошибка: задача пересекается с другими задачами.";
        sendText(exchange, response, 406);
    }

    protected void sendServerError(HttpExchange exchange, Exception e) throws IOException {
        String response = "Внутренняя ошибка сервера: " + e.getMessage();
        sendText(exchange, response, 500);
    }
}
